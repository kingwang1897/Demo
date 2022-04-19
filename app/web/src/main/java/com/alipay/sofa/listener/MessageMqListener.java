package com.alipay.sofa.listener;

import com.alipay.sofa.manager.MessageVerifyManager;
import com.alipay.sofa.model.HelpResult;
import com.alipay.sofa.model.MessageResult;
import com.alipay.sofa.service.MessageGenerateService;
import com.alipay.sofa.service.MessageHandleService;
import com.alipay.sofa.service.MessageParseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageMqListener {

    @Autowired
    private MessageParseService messageParseService;

    @Autowired
    private MessageHandleService messageHandleService;

    @Autowired
    private MessageGenerateService messageGenerateService;

    @Autowired
    private MessageVerifyManager messageVerifyManager;

    public void messageHandle(String pkt) {
        // step 1: parse pkt
        MessageResult messageResult = messageParseService.parsePkt(pkt);

        // step 2: handle pkt
        HelpResult helpResult = messageHandleService.messageHandle(messageResult);

        // step 2: response message
        HelpResult<MessageResult> response = messageGenerateService.generateMsgByXml(messageResult, helpResult);
        if (!response.isSuccess()) {
            return;
        }

        // step 3: verify response
        System.out.println(response.getData().getMessage());
        messageVerifyManager.parse(response.getData().getMessage());
    }
}
