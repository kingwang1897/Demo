package com.stori.demo.processor.impl;

import com.stori.demo.processor.constant.Constant;
import com.stori.demo.processor.constant.MessageStatus;
import com.stori.demo.processor.model.HelpResult;
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

    @Override
    public MessageLifecycle messageHandle(MessageLifecycle messageLifecycle) {
        messageLifecycle.setCallCount(messageLifecycle.getCallCount() + Constant.MESSAGE_CALL_INIT);
        messageLifecycle.setStatus(MessageStatus.getNextStatus(messageLifecycle.getStatus()));
        messageLifecycle.setMessageProcessorTime(System.currentTimeMillis());

        // step 1: handle messageResult
        HelpResult<MessageHandle> helpResult;
        switch (CommonUtil.convertHexToString(messageLifecycle.getMessageResult().getType())) {
            case Constant.MESSAGE_TYPE_ID_MANGER:
                helpResult = handleForManager(messageLifecycle.getMessageResult());
                break;
            case Constant.MESSAGE_TYPE_ID_USER:
                helpResult = handleForUser(messageLifecycle.getMessageResult());
                break;
            case Constant.MESSAGE_TYPE_ID_NOTIFY:
                helpResult = handleForNotify(messageLifecycle.getMessageResult());
                break;
            default:
                logger.error("messageHandle error, case by: no message type, type is:{}.", CommonUtil.convertHexToString(messageLifecycle.getMessageResult().getType()));
                messageLifecycle.setStatus(MessageStatus.FAILURE);
                return messageLifecycle;
        }

        if (!helpResult.isSuccess()) {
            messageLifecycle.setStatus(MessageStatus.getNextStatus(messageLifecycle.getStatus()));
            messageLifecycle.setHelpResult(helpResult);
            return messageLifecycle;
        }

        messageLifecycle.setHelpResult(helpResult);
        messageLifecycle.setStatus(MessageStatus.getNextStatus(messageLifecycle.getStatus()));
        messageLifecycle.setCallCount(Constant.MESSAGE_CALL_INIT);
        return messageLifecycle;
    }

    @Override
    public HelpResult<MessageHandle>  handleForNotify(MessageResult messageResult) {
        return HelpResult.ok(new MessageHandle(messageResult.getType()));
    }

    @Override
    public HelpResult<MessageHandle>  handleForManager(MessageResult messageResult) {
        MessageHandle messageHandle = new MessageHandle(messageResult.getType());
        Map<Integer, String> messageFileds = new HashMap<>();
        messageHandle.setMessageFileds(messageFileds);
        messageFileds.put(Constant.MESSAGE_RESPONSE_ID, Constant.MESSAGE_RESULT_SUCCESS);
        return HelpResult.ok(messageHandle);
    }

    @Override
    public HelpResult<MessageHandle>  handleForUser(MessageResult messageResult){
        MessageHandle messageHandle = new MessageHandle(messageResult.getType());
        Map<Integer, String> messageFileds = new HashMap<>();
        messageHandle.setMessageFileds(messageFileds);
        messageFileds.put(Constant.MESSAGE_RESPONSE_ID, Constant.MESSAGE_RESULT_SUCCESS);
        messageFileds.put(Constant.MESSAGE_RESPONSE_AUTH_ID, Constant.MESSAGE_RESPONSE_AUTH_SUCCESS);
        return HelpResult.ok(messageHandle);
    }
}
