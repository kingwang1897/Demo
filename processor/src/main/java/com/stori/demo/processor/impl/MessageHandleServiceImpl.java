package com.stori.demo.processor.impl;

import com.stori.demo.processor.constant.Constant;
import com.stori.demo.processor.constant.MessageStatus;
import com.stori.demo.processor.model.MessageHandle;
import com.stori.demo.processor.model.MessageLifecycle;
import com.stori.demo.processor.model.MessageResult;
import com.stori.demo.processor.model.Result;
import com.stori.demo.processor.service.MessageBaseService;
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
public class MessageHandleServiceImpl extends MessageBaseService implements MessageHandleService {
    protected final Logger logger = LoggerFactory.getLogger(MessageParseServiceImpl.class);

    /**
     * handle message
     *
     * @param messageLifecycle
     * @return
     */
    @Override
    public MessageLifecycle execute(MessageLifecycle messageLifecycle) {
        if (!executeStart(messageLifecycle, MessageStatus.HANDLEING)) {
            return messageLifecycle;
        }

        // step 1: handle messageResult
        Result<MessageHandle> result;
        switch (CommonUtil.convertHexToString(messageLifecycle.getMessageResult().getType())) {
            case Constant.MESSAGE_TYPE_ID_MANGER:
                result = handleForManager(messageLifecycle.getMessageResult());
                break;
            case Constant.MESSAGE_TYPE_ID_USER:
                result = handleForUser(messageLifecycle.getMessageResult());
                break;
            case Constant.MESSAGE_TYPE_ID_NOTIFY:
                result = handleForNotify(messageLifecycle.getMessageResult());
                break;
            default:
                logger.error("messageHandle error, case by: no message type, type is:{}.", CommonUtil.convertHexToString(messageLifecycle.getMessageResult().getType()));
                messageLifecycle.setStatus(MessageStatus.FAILURE);
                return messageLifecycle;
        }

        // step 2: failure result
        if (!result.isSuccess()) {
            executeError(messageLifecycle, result);
            return messageLifecycle;
        }

        // step 3: success result
        messageLifecycle.setResult(result);
        executeEnd(messageLifecycle, null, result);
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
