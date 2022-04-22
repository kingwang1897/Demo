package com.stori.demo.processor.service;


import com.stori.demo.processor.model.Result;
import com.stori.demo.processor.model.MessageHandle;
import com.stori.demo.processor.model.MessageResult;

public interface MessageHandleService {

    Result<MessageHandle>  handleForNotify(MessageResult messageResult);

    Result<MessageHandle>  handleForManager(MessageResult messageResult);

    Result<MessageHandle>  handleForUser(MessageResult messageResult);
}
