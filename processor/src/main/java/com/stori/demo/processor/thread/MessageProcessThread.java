package com.stori.demo.processor.thread;

import com.google.common.base.Throwables;
import com.stori.demo.processor.constant.Constant;
import com.stori.demo.processor.constant.MessageStatus;
import com.stori.demo.processor.impl.MessageHandleServiceImpl;
import com.stori.demo.processor.impl.MessageParseServiceImpl;
import com.stori.demo.processor.impl.MessageResponseServiceImpl;
import com.stori.demo.processor.impl.MessageSendServiceImpl;
import com.stori.demo.processor.model.MessageLifecycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class MessageProcessThread extends Thread {
    protected final Logger logger = LoggerFactory.getLogger(MessageProcessThread.class);

    private Map<String, MessageLifecycle> concurrentHashMap;

    private MessageParseServiceImpl messageParseService;

    private MessageHandleServiceImpl messageHandleService;

    private MessageResponseServiceImpl messageResponseService;

    private MessageSendServiceImpl messageSendService;

    public MessageProcessThread (MessageParseServiceImpl messageParseService,
                                 MessageHandleServiceImpl messageHandleService,
                                 MessageResponseServiceImpl messageResponseService,
                                 MessageSendServiceImpl messageSendService,
                                 Map<String, MessageLifecycle> concurrentHashMap) {
        this.messageParseService = messageParseService;
        this.messageHandleService = messageHandleService;
        this.messageResponseService = messageResponseService;
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
        switch (messageLifecycle.getStatus()) {
            case PREPARSE:
                messageParseService.execute(messageLifecycle);
                break;
            case PREHANDLE:
                messageHandleService.execute(messageLifecycle);
                break;
            case PRERESPONSE:
                messageResponseService.execute(messageLifecycle);
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
