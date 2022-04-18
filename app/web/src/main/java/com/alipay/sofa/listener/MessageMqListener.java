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
        String responseMessage = messageGenerateService.generateMsgByConfig(messageResult, helpResult);
        // 2e123036353834383031303030303030303030303130303030303030000000003030303030303030003030303030303831308220000002000800040000001000000130343133313430303030343830373532303031363030303030303030303030303030313031303839323031303030304142434445464748
        System.out.println(responseMessage);

        // step 3: verify response
        messageVerifyManager.parse(responseMessage);
    }
}
