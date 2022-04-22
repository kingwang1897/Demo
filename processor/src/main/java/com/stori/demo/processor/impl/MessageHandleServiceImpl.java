package com.stori.demo.processor.impl;

import com.stori.demo.processor.constant.Constant;
import com.stori.demo.processor.constant.MessageStatus;
import com.stori.demo.processor.model.Result;
import com.stori.demo.processor.model.MessageHandle;
import com.stori.demo.processor.model.MessageLifecycle;
import com.stori.demo.processor.model.MessageResult;
import com.stori.demo.processor.service.MessageHandleService;
import com.stori.demo.processor.util.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * message handle
 *
 */
@Service("messageHandleService")
public class MessageHandleServiceImpl implements MessageHandleService {
    protected final Logger logger = LoggerFactory.getLogger(MessageParseServiceImpl.class);

    /**
     * handle message
     *
     * @param messageLifecycle
     * @return
     */
    @Override
    public MessageLifecycle execute(MessageLifecycle messageLifecycle) {
        if (messageLifecycle.getStatus().equals(MessageStatus.HANDLEING)) {
            return messageLifecycle;
        }
        messageLifecycle.setCallCount(messageLifecycle.getCallCount() + Constant.MESSAGE_CALL_INIT);
        messageLifecycle.setStatus(MessageStatus.getNextStatus(messageLifecycle.getStatus()));
        messageLifecycle.setMessageProcessorTime(System.currentTimeMillis());

        // step 1: handle messageResult
        Result<MessageHandle> Result;
        switch (CommonUtil.convertHexToString(messageLifecycle.getMessageResult().getType())) {
            case Constant.MESSAGE_TYPE_ID_MANGER:
                Result = handleForManager(messageLifecycle.getMessageResult());
                break;
            case Constant.MESSAGE_TYPE_ID_USER:
                Result = handleForUser(messageLifecycle.getMessageResult());
                break;
            case Constant.MESSAGE_TYPE_ID_NOTIFY:
                Result = handleForNotify(messageLifecycle.getMessageResult());
                break;
            default:
                logger.error("messageHandle error, case by: no message type, type is:{}.", CommonUtil.convertHexToString(messageLifecycle.getMessageResult().getType()));
                messageLifecycle.setStatus(MessageStatus.FAILURE);
                return messageLifecycle;
        }

        // step 2: handle result
        if (!Result.isSuccess()) {
            messageLifecycle.setStatus(MessageStatus.getNextStatus(messageLifecycle.getStatus()));
            messageLifecycle.setResult(Result);
            return messageLifecycle;
        }

        messageLifecycle.setResult(Result);
        messageLifecycle.setStatus(MessageStatus.getNextStatus(messageLifecycle.getStatus()));
        messageLifecycle.setCallCount(Constant.MESSAGE_CALL_INIT);
        return messageLifecycle;
    }

    @Override
    public Result<MessageHandle>  handleForNotify(MessageResult messageResult) {
        return Result.ok(new MessageHandle(messageResult.getType()));
    }

    @Override
    public Result<MessageHandle>  handleForManager(MessageResult messageResult) {
        MessageHandle messageHandle = new MessageHandle(messageResult.getType());
        Map<Integer, String> messageFileds = new HashMap<>();
        messageHandle.setMessageFileds(messageFileds);
        messageFileds.put(Constant.MESSAGE_RESPONSE_ID, Constant.MESSAGE_RESULT_SUCCESS);
        return Result.ok(messageHandle);
    }

    @Override
    public Result<MessageHandle>  handleForUser(MessageResult messageResult){
        MessageHandle messageHandle = new MessageHandle(messageResult.getType());
        Map<Integer, String> messageFileds = new HashMap<>();
        messageHandle.setMessageFileds(messageFileds);
        messageFileds.put(Constant.MESSAGE_RESPONSE_ID, Constant.MESSAGE_RESULT_SUCCESS);
        messageFileds.put(Constant.MESSAGE_RESPONSE_AUTH_ID, Constant.MESSAGE_RESPONSE_AUTH_SUCCESS);
        return Result.ok(messageHandle);
    }
}
