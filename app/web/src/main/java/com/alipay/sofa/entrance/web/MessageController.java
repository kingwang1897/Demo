package com.alipay.sofa.entrance.web;

import com.alibaba.fastjson.JSONObject;
import com.alipay.sofa.constant.IsoFields;
import com.alipay.sofa.entrance.web.service.MessageService;
import com.alipay.sofa.model.StoriMessage;
import com.alipay.sofa.mq.producer.service.MqProducerService;
import com.alipay.sofa.util.MessageTester;
import com.solab.iso8583.IsoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;
import java.util.Objects;

@RestController
public class MessageController implements InitializingBean {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private MessageService messageService;
    @Resource
    private MessageTester defaultMessageTester;

    @Autowired
    private MqProducerService mqProducerService;

    private JSONObject templates;

    @Override
    public void afterPropertiesSet() throws Exception {
        this.templates = messageService.loadTemplatesFromResource();
        System.out.println("load templates " + this.templates);
        if (Objects.isNull(this.templates)) {
            throw new RuntimeException("load templates failed.");
        }
    }

    @PostMapping("/message/json")
    public JSONObject handleJson(@RequestBody JSONObject request) {
        logger.info("Received json message {}", request);
        // for debug
        request.put("0", "0110");
        request.put("38", "123323");
        request.put("39", "00");
        // generate iso message
        IsoMessage isoMessage = messageService.of(request);
        String hex = isoMessage.toCupsAsciiHexMessageWithOutHeader();
        // TODO
        // require put to mq queue for processing.
        StoriMessage storiMessage = new StoriMessage();
        // TODO ???????????????originMessage???isoMessage????????????????????? @??????

        storiMessage.setOriginMessage(hex);

        boolean sendResult = mqProducerService.sendMessage(JSONObject.toJSONString(storiMessage));
        if (!sendResult) {
            // TODO ???????????????????????????????????????
        }

        // receive result from the output

        // return resp to front

        String info = defaultMessageTester.parse(hex);
        JSONObject resp = new JSONObject();
        resp.put("sendResult", sendResult);
        resp.put("request", request);
        resp.put("requestHex", hex);
        resp.put("requestInfo", info);
        resp.put("response", "key:value");
        resp.put("responseHex", "origin str from output");
        resp.put("responseInfo", "response info");
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
        return this.templates;
    }
}

