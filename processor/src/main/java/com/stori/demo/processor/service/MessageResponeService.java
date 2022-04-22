package com.stori.demo.processor.service;


import com.stori.demo.processor.model.Result;
import com.stori.demo.processor.model.MessageResult;

/**
 * Message response
 */
public interface MessageResponeService extends MessageBaseService {

    /**
     * generate by config
     *
     * @return
     */
    Result<MessageResult> generateMsgByConfig(MessageResult messageResult, Result result);
}
