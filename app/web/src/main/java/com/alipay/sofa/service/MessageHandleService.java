package com.alipay.sofa.service;

import com.alipay.sofa.model.HelpResult;
import com.alipay.sofa.model.MessageResult;

public interface MessageHandleService {

    HelpResult messageHandle(MessageResult messageResult);

    HelpResult handleForNotify(MessageResult messageResult);

    HelpResult handleForManager(MessageResult messageResult);

    HelpResult handleForUser(MessageResult messageResult);
}
