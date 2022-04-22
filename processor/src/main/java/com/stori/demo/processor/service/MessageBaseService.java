package com.stori.demo.processor.service;


import com.stori.demo.processor.constant.Constant;
import com.stori.demo.processor.constant.MessageStatus;
import com.stori.demo.processor.model.MessageLifecycle;
import com.stori.demo.processor.model.MessageResult;
import com.stori.demo.processor.model.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class MessageBaseService {
    protected final Logger logger = LoggerFactory.getLogger(MessageBaseService.class);

    /**
     * execute message(parse/handle/response/send)
     *
     * @return
     */
    public abstract MessageLifecycle execute(MessageLifecycle messageLifecycle);

    /**
     * execute start
     *
     * @param messageLifecycle
     * @param messageStatus
     * @return
     */
    public boolean executeStart(MessageLifecycle messageLifecycle, MessageStatus messageStatus) {
        // retry more than 3
        if (messageLifecycle.getCallCount() > Constant.MESSAGE_CALL_RETRY) {
            logger.error("messageProcess error, cause by: retry more 3 times, status is : {}, messageId is: {}.", messageLifecycle.getStatus().name(), messageLifecycle.getMessageId());
            // sending fail, update DONE, other update FAILURE.
            messageLifecycle.setStatus(messageLifecycle.getStatus().equals(MessageStatus.SENDING) ? MessageStatus.DONE : MessageStatus.FAILURE);
            messageLifecycle.setCallCount(Constant.MESSAGE_CALL_INIT);
            return false;
        }

        // check task status
        if (messageLifecycle.getStatus().equals(messageStatus)) {
            return false;
        }

        messageLifecycle.setCallCount(messageLifecycle.getCallCount() + Constant.MESSAGE_CALL_INIT);
        messageLifecycle.setStatus(MessageStatus.getNextStatus(messageLifecycle.getStatus()));
        messageLifecycle.setMessageProcessorTime(System.currentTimeMillis());
        return true;
    }

    /**
     * execute end
     *
     * @param messageLifecycle
     * @param messageResult
     * @param result
     */
    public void executeEnd(MessageLifecycle messageLifecycle, MessageResult messageResult, Result result) {
        if (messageResult != null) {
            messageLifecycle.setMessageResult(messageResult);
        }

        if (result != null) {
            messageLifecycle.setResult(result);
        }

        messageLifecycle.setStatus(MessageStatus.getNextStatus(messageLifecycle.getStatus()));
        messageLifecycle.setCallCount(Constant.MESSAGE_CALL_INIT);
    }

    /**
     *
     *
     * @param messageLifecycle
     */
    public void executeError(MessageLifecycle messageLifecycle, Result result) {
        if (result != null) {
            messageLifecycle.setResult(result);
        }

        messageLifecycle.setStatus(MessageStatus.getPreStatus(messageLifecycle.getStatus()));
    }
}
