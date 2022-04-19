package com.stori.demo.processor.service;


import com.stori.demo.processor.model.HelpResult;
import com.stori.demo.processor.model.MessageLifecycle;
import com.stori.demo.processor.model.MessageResult;

public interface MessageHandleService {

    MessageLifecycle messageHandle(MessageLifecycle messageLifecycle);

    HelpResult handleForNotify(MessageResult messageResult);

    HelpResult handleForManager(MessageResult messageResult);

    HelpResult handleForUser(MessageResult messageResult);
}
