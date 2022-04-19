package com.alipay.sofa.entrance.web;

import com.alibaba.fastjson.JSONObject;
import com.alipay.sofa.constant.IsoFields;
import com.alipay.sofa.entrance.web.service.MessageService;
import com.alipay.sofa.util.MessageTester;
import com.solab.iso8583.IsoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;

@RestController
public class MessageController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private MessageService messageService;
    @Resource
    private MessageTester defaultMessageTester;
    // TODO
    // Processing Component

    @PostMapping("/message/json")
    public JSONObject handleJson(@RequestBody JSONObject request) {
        logger.info("Received json message {}", request);
        // for debug
        request.put("0", "0110");
        request.put("38", "123323");
        request.put("39", "00");
        // generate iso message
        IsoMessage isoMessage = messageService.of(request);
        // TODO
        // require put to mq queue for processing.

        // receive result from the output

        // return resp to front
        String hex = isoMessage.toCupsAsciiHexMessageWithOutHeader();
        String info = defaultMessageTester.parse(hex);
        JSONObject resp = new JSONObject();
        resp.put("request", request);
        resp.put("requestHex", hex);
        resp.put("requestInfo", info);
        resp.put("response", "key:value");
        resp.put("responseHex", "origin str from output");
        return resp;
    }

    @PostMapping("/message/raw")
    public String handleRaw() {
        return "received";
    }

    @GetMapping("/message/fields")
    public JSONObject fields() {
        return IsoFields.json();
    }

    @GetMapping("/message/templates")
    public JSONObject templates() throws IOException {
        return messageService.loadTemplatesFromResource();
    }
}

