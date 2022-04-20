package com.alipay.sofa.entrance.socket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.sofa.entrance.socket.server.WebSocketServer;
import com.alipay.sofa.entrance.web.service.MessageService;
import com.alipay.sofa.model.StoriMessage;
import com.alipay.sofa.mq.producer.service.MqProducerService;
import com.alipay.sofa.util.MessageTester;
import com.solab.iso8583.IsoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class WebSocketMessageHandler {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Resource
    private MessageService messageService;
    @Resource
    private MqProducerService mqProducerService;
    @Resource
    private MessageTester defaultMessageTester;


    public void handleRequest(String socketId, JSONObject request) {
        logger.info("Received json message {}", request);
        JSONObject message = request.getJSONObject("message");
        String messageId = request.getString("messageId");
        logger.info("handling socketId:{} messageId:{} message:{}", socketId, messageId, message);
        // generate iso message
        IsoMessage isoMessage = messageService.of(message);
        String hex = isoMessage.toCupsAsciiHexMessageWithOutHeader();
        // require put to mq queue for processing.
        StoriMessage storiMessage = new StoriMessage();
        //   填充消息，originMessage为isoMessage编码后的字符串 @尚武
        storiMessage.setSocketId(socketId);
        storiMessage.setMessageId(messageId);
        storiMessage.setOriginMessage(hex);
        //send to mq
        boolean sendResult = mqProducerService.sendMessage(JSONObject.toJSONString(storiMessage));
        if (!sendResult) {
            // TODO 发送消息异常处理，直接返回
        }
        // TODO for debug, need remove after processing service completed.
        String info = defaultMessageTester.parse(hex);

        JSONObject response = (JSONObject) message.clone();
        response.put("0", "0110");
        response.put("38", "123323");
        response.put("39", "00");

        JSONObject respMessage = new JSONObject();
        respMessage.put("request", message);
        respMessage.put("requestHex", hex);
        respMessage.put("requestInfo", info);
        // put response
        respMessage.put("response", response);
        respMessage.put("responseHex", "origin str from output");
        respMessage.put("responseInfo", "response info");

        JSONObject responseBody = new JSONObject();
        responseBody.put("code", 10002);
        responseBody.put("messageId", messageId);
        responseBody.put("data", respMessage);
        WebSocketServer.sendMessage(socketId, JSON.toJSONString(responseBody));
        // debug End
    }

    public void handleResponse(StoriMessage storiMessage) {
        // TODO return to front
        System.out.println("handling " + storiMessage);
        // JSONObject respMessage = new JSONObject();
        // respMessage.put("sendResult", sendResult);
        // respMessage.put("request", request);
        // respMessage.put("requestHex", hex);
        // respMessage.put("requestInfo", info);
        // respMessage.put("response", "key:value");
        // respMessage.put("responseHex", "origin str from output");
        // respMessage.put("responseInfo", "response info");
        // resp.put("message", respMessage);
    }

}

