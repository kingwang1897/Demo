package com.stori.demo.processor.service;


import com.stori.demo.processor.model.HelpResult;
import com.stori.demo.processor.model.MessageLifecycle;
import com.stori.demo.processor.model.MessageResult;

/**
 * Message Generate
 */
public interface MessageGenerateService {

    /**
     * generate by xml
     *
     * @return
     */
    MessageLifecycle generateMsgByXml(MessageLifecycle messageLifecycle);

    /**
     * generate by config
     *
     * @return
     */
    HelpResult<MessageResult> generateMsgByConfig(MessageResult messageResult, HelpResult result);
}
