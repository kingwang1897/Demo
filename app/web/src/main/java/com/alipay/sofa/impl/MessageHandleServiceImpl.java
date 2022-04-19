package com.alipay.sofa.impl;

import com.alipay.sofa.constant.Constant;
import com.alipay.sofa.model.HelpResult;
import com.alipay.sofa.model.MessageResult;
import com.alipay.sofa.service.MessageHandleService;
import com.alipay.sofa.util.CommonUtil;
import org.springframework.stereotype.Service;

/**
 * message handle
 *
 */
@Service("messageHandleService")
public class MessageHandleServiceImpl implements MessageHandleService {

    @Override
    public HelpResult messageHandle(MessageResult messageResult) {
        // step 1: handle messageResult
        HelpResult helpResult;
        switch (CommonUtil.convertHexToString(messageResult.getType())) {
            case Constant.MESSAGE_TYPE_ID_MANGER:
                helpResult = handleForManager(messageResult);
                break;
            case Constant.MESSAGE_TYPE_ID_USER:
                helpResult = handleForUser(messageResult);
                break;
            case Constant.MESSAGE_TYPE_ID_NOTIFY:
                helpResult = handleForNotify(messageResult);
                break;
            default:
                helpResult = HelpResult.fail("error.no.message.type");
                break;
        }

        return helpResult;
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
