package com.stori.demo.processor.thread;

import com.google.common.base.Throwables;
import com.stori.demo.processor.constant.Constant;
import com.stori.demo.processor.constant.MessageStatus;
import com.stori.demo.processor.model.MessageLifecycle;
import com.stori.demo.processor.service.MessageHandleService;
import com.stori.demo.processor.service.MessageParseService;
import com.stori.demo.processor.service.MessageResponeService;
import com.stori.demo.processor.service.MessageSendService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class MessageProcessThread extends Thread {
    protected final Logger logger = LoggerFactory.getLogger(MessageProcessThread.class);

    private Map<String, MessageLifecycle> concurrentHashMap;

    private MessageParseService messageParseService;

    private MessageHandleService messageHandleService;

    private MessageResponeService messageResponeService;

    private MessageSendService messageSendService;

    public MessageProcessThread (MessageParseService messageParseService,
                                 MessageHandleService messageHandleService,
                                 MessageResponeService messageResponeService,
                                 MessageSendService messageSendService,
                                 Map<String, MessageLifecycle> concurrentHashMap) {
        this.messageParseService = messageParseService;
        this.messageHandleService = messageHandleService;
        this.messageResponeService = messageResponeService;
        this.messageSendService = messageSendService;
        this.concurrentHashMap = concurrentHashMap;
    }

    @Override
    public void run() {
        try {
            while (true) {
                for (Map.Entry<String, MessageLifecycle> entry : concurrentHashMap.entrySet()) {
                    messageProcess(entry.getValue());
                }

                Thread.sleep(500);
            }
        } catch (Exception e) {
            logger.error("MessageProcessThread error, cause by: {}.", Throwables.getStackTraceAsString(e));
        }
    }

    private void messageProcess(MessageLifecycle messageLifecycle) {
        // retry more than 3
        if (messageLifecycle.getCallCount() > Constant.MESSAGE_CALL_RETRY && !messageLifecycle.getStatus().equals(MessageStatus.DONE)
        && !messageLifecycle.getStatus().equals(MessageStatus.FAILURE)) {
            logger.error("messageProcess error, cause by: retry more 3 times, status is : {}, messageId is: {}.", messageLifecycle.getStatus().name(), messageLifecycle.getMessageId());
            // sending fail, update DONE, other update FAILURE.
            messageLifecycle.setStatus(messageLifecycle.getStatus().equals(MessageStatus.SENDING) ? MessageStatus.DONE : MessageStatus.FAILURE);
            messageLifecycle.setCallCount(Constant.MESSAGE_CALL_INIT);
            return;
        }

        switch (messageLifecycle.getStatus()) {
            case PREPARSE:
                messageParseService.execute(messageLifecycle);
                break;
            case PREHANDLE:
                messageHandleService.execute(messageLifecycle);
                break;
            case PRERESPONSE:
                messageResponeService.execute(messageLifecycle);
                break;
            case PRESEND:
                messageSendService.execute(messageLifecycle);
                break;
            case FAILURE:
                logger.error("messageProcess error, messageLifecycle id is: {}.", messageLifecycle.getStatus().getName());
                // add failure status
                messageLifecycle.getMessageResult().getMessageFileds().put(Constant.MESSAGE_CODE_ERROR, messageLifecycle.getStatus().name());
                messageLifecycle.setStatus(MessageStatus.PRESEND);
                break;
            case DONE:
                concurrentHashMap.remove(messageLifecycle.getMessageId());
                break;
            default:
                if (System.currentTimeMillis() > messageLifecycle.getMessageProcessorTime() + messageLifecycle.getStatus().getTimeout()) {
                    messageLifecycle.setStatus(MessageStatus.getPreStatus(messageLifecycle.getStatus()));
                }
                break;
        }
    }
}
