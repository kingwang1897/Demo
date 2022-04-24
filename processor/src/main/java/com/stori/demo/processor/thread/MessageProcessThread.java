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

public class MessageProcessThread extends Thread {
    protected final Logger logger = LoggerFactory.getLogger(MessageProcessThread.class);

    private MessageParseServiceImpl messageParseService;

    private MessageHandleServiceImpl messageHandleService;

    private MessageResponseServiceImpl messageResponseService;

    private MessageSendServiceImpl messageSendService;

    private MessageLifecycle messageLifecycle;

    public MessageProcessThread(MessageParseServiceImpl messageParseService,
                                MessageHandleServiceImpl messageHandleService,
                                MessageResponseServiceImpl messageResponseService,
                                MessageSendServiceImpl messageSendService,
                                MessageLifecycle messageLifecycle) {
        this.messageParseService = messageParseService;
        this.messageHandleService = messageHandleService;
        this.messageResponseService = messageResponseService;
        this.messageSendService = messageSendService;
        this.messageLifecycle = messageLifecycle;
    }

    @Override
    public void run() {
        try {
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
                    break;
                default:
                    if (System.currentTimeMillis() > messageLifecycle.getMessageProcessorTime() + messageLifecycle.getStatus().getTimeout()) {
                        messageLifecycle.setStatus(MessageStatus.getPreStatus(messageLifecycle.getStatus()));
                    }
                    break;
            }
        } catch (Exception e) {
            logger.error("MessageProcessThread error, cause by: {}.", Throwables.getStackTraceAsString(e));
        }
    }
}
