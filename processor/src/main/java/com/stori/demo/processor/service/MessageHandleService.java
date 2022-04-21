package com.stori.demo.processor.service;


import com.stori.demo.processor.model.HelpResult;
import com.stori.demo.processor.model.MessageHandle;
import com.stori.demo.processor.model.MessageLifecycle;
import com.stori.demo.processor.model.MessageResult;

public interface MessageHandleService {

    MessageLifecycle messageHandle(MessageLifecycle messageLifecycle);

    HelpResult<MessageHandle>  handleForNotify(MessageResult messageResult);

    HelpResult<MessageHandle>  handleForManager(MessageResult messageResult);

    HelpResult<MessageHandle>  handleForUser(MessageResult messageResult);
}
