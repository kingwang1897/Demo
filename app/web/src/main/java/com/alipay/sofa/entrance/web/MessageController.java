package com.alipay.sofa.entrance.web;

import com.alibaba.fastjson.JSONObject;
import com.alipay.sofa.constant.IsoFields;
import com.alipay.sofa.entrance.web.service.MessageService;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@RestController
public class MessageController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private MessageService messageService;
    // TODO
    // Processing Component

    @PostMapping("/message/json")
    public JSONObject handleJson(@RequestBody JSONObject request) {
        request.put("0", "0110");
        request.put("38", "123323");
        request.put("39", "00");
        return request;
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

