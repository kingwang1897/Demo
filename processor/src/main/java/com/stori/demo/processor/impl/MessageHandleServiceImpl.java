package com.stori.demo.processor.impl;

import com.stori.demo.processor.constant.Constant;
import com.stori.demo.processor.constant.MessageStatus;
import com.stori.demo.processor.model.HelpResult;
import com.stori.demo.processor.model.MessageLifecycle;
import com.stori.demo.processor.model.MessageResult;
import com.stori.demo.processor.service.MessageHandleService;
import com.stori.demo.processor.util.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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
        HelpResult helpResult;
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

        messageLifecycle.setHelpResult(helpResult);
        messageLifecycle.setStatus(MessageStatus.getNextStatus(messageLifecycle.getStatus()));
        messageLifecycle.setCallCount(Constant.MESSAGE_CALL_INIT);
        return messageLifecycle;
    }

    @Override
    public HelpResult handleForNotify(MessageResult messageResult) {
        return HelpResult.ok(true);
    }

    @Override
    public HelpResult handleForManager(MessageResult messageResult) {
        return HelpResult.ok(true);
    }

    @Override
    public HelpResult handleForUser(MessageResult messageResult){
        return HelpResult.ok("123323");
    }
}
