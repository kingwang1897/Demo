package com.stori.demo.processor.thread;

import com.google.common.base.Throwables;
import com.stori.demo.processor.constant.Constant;
import com.stori.demo.processor.constant.MessageStatus;
import com.stori.demo.processor.listener.MessageMqSender;
import com.stori.demo.processor.model.MessageLifecycle;
import com.stori.demo.processor.service.MessageGenerateService;
import com.stori.demo.processor.service.MessageHandleService;
import com.stori.demo.processor.service.MessageParseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class MessageProcessThread extends Thread {
    protected final Logger logger = LoggerFactory.getLogger(MessageProcessThread.class);

    private Map<String, MessageLifecycle> concurrentHashMap;

    private MessageParseService messageParseService;

    private MessageHandleService messageHandleService;

    private MessageGenerateService messageGenerateService;

    private MessageMqSender messageMqSender;

    public MessageProcessThread (MessageParseService messageParseService,
                                 MessageHandleService messageHandleService,
                                 MessageGenerateService messageGenerateService,
                                 MessageMqSender messageMqSender,
                                 Map<String, MessageLifecycle> concurrentHashMap) {
        this.messageParseService = messageParseService;
        this.messageHandleService = messageHandleService;
        this.messageGenerateService = messageGenerateService;
        this.messageMqSender = messageMqSender;
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
                messageParseService.parsePkt(messageLifecycle);
                break;
            case PREHANDLE:
                messageHandleService.messageHandle(messageLifecycle);
                break;
            case PRERESPONSE:
                messageGenerateService.generateMsgByXml(messageLifecycle);
                break;
            case PRESEND:
                messageMqSender.messageSender(messageLifecycle);
                break;
            case DONE:
            case FAILURE:
                concurrentHashMap.remove(messageLifecycle.getMessageId());
                break;
            default:
                if (messageLifecycle.getCallCount() > Constant.MESSAGE_CALL_RETRY) {
                    // retry more than 3
                    logger.error("messageProcess error, cause by: retry more 3 times, status is : {}, messageId is: {}.", messageLifecycle.getStatus().name(), messageLifecycle.getMessageId());
                    messageLifecycle.setStatus(MessageStatus.FAILURE);
                    return;
                }

                if (System.currentTimeMillis() > messageLifecycle.getMessageProcessorTime() + messageLifecycle.getStatus().getTimeout()) {
                    messageLifecycle.setStatus(MessageStatus.getPreStatus(messageLifecycle.getStatus()));
                }
                break;
        }
    }
}
