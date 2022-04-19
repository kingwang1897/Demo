package com.stori.demo.processor.impl;

import com.stori.demo.processor.constant.Constant;
import com.stori.demo.processor.constant.MessageStatus;
import com.stori.demo.processor.model.HelpResult;
import com.stori.demo.processor.model.MessageLifecycle;
import com.stori.demo.processor.model.MessageResult;
import com.stori.demo.processor.service.MessageHandleService;
import com.stori.demo.processor.util.CommonUtil;
import org.springframework.stereotype.Service;

/**
 * message handle
 *
 */
@Service("messageHandleService")
public class MessageHandleServiceImpl implements MessageHandleService {

    @Override
    public MessageLifecycle messageHandle(MessageLifecycle messageLifecycle) {
        messageLifecycle.setStatus(MessageStatus.getNextStatus(messageLifecycle.getStatus()));

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
                helpResult = HelpResult.fail("error.no.message.type");
                messageLifecycle.setStatus(MessageStatus.FAILURE);
                return messageLifecycle;
        }

        messageLifecycle.setHelpResult(helpResult);
        messageLifecycle.setStatus(MessageStatus.getNextStatus(messageLifecycle.getStatus()));
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
