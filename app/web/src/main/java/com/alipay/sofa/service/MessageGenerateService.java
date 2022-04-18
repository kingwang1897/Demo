package com.alipay.sofa.service;

import com.alipay.sofa.model.HelpResult;
import com.alipay.sofa.model.MessageResult;

/**
 * Message Generate
 */
public interface MessageGenerateService {

    /**
     * generate by xml
     *
     * @return
     */
    MessageResult generateMsgByXml(MessageResult messageResult, HelpResult result);

    /**
     * generate by config
     *
     * @return
     */
    String generateMsgByConfig(MessageResult messageResult, HelpResult result);
}
