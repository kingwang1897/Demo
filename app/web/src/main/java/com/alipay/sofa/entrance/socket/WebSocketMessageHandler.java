package com.alipay.sofa.entrance.socket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.sofa.entrance.socket.server.WebSocketServer;
import com.alipay.sofa.entrance.web.service.MessageService;
import com.alipay.sofa.model.StoriMessage;
import com.alipay.sofa.mq.producer.service.MqProducerService;
import com.alipay.sofa.util.MessageTester;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import com.solab.iso8583.IsoMessage;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Component
public class WebSocketMessageHandler {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Resource
    private MessageService messageService;
    @Resource
    private MqProducerService mqProducerService;
    // @Resource
    // private MessageTester defaultMessageTester;
    /**
     * Store the request by messageId for matching the response.
     */
    private Cache<String, JSONObject> messageStorage = CacheBuilder.newBuilder()
            .maximumSize(3000)
            .expireAfterWrite(24, TimeUnit.HOURS)
            .build();


    public void handleRequest(String socketId, JSONObject request) {
        logger.info("Received json message {}", request);
        JSONObject message = request.getJSONObject("message");
        String messageId = request.getString("messageId");
        boolean debug = request.getBoolean("debug");
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
        //   String info = defaultMessageTester.parse(hex);
        // TODO for debug, need remove after processing service completed.
        // debug start
        if (debug) {
            // create response message
            JSONObject respMessage = new JSONObject();
            respMessage.put("request", message);
            respMessage.put("requestHex", hex);
            // respMessage.put("requestInfo", info);
            // mock response
            JSONObject response = (JSONObject) message.clone();
            response.put("0", "0110");
            response.put("38", "123323");
            response.put("39", "00");
            // put response
            respMessage.put("response", response);
            respMessage.put("responseHex", "origin str from output");
            respMessage.put("responseInfo", "response info");
            // create response body
            JSONObject responseBody = new JSONObject();
            responseBody.put("code", 10002);
            responseBody.put("messageId", messageId);
            responseBody.put("data", respMessage);
            WebSocketServer.sendMessage(socketId, JSON.toJSONString(responseBody));
            return;
        }
        // debug End
        // send to mq
        boolean send = mqProducerService.sendMessage(JSONObject.toJSONString(storiMessage));
        if (send) {
            // Store the request
            request.put("requestHex", hex);
            // request.put("requestInfo", info);
            messageStorage.put(messageId, request);
        } else {
            // Fail to front
            JSONObject responseBody = new JSONObject();
            responseBody.put("code", 10003);
            responseBody.put("messageId", messageId);
            responseBody.put("reason", "send to queue failed.");
            WebSocketServer.sendMessage(socketId, JSON.toJSONString(responseBody));
            return;
        }
    }

    public void handleResponse(StoriMessage storiMessage) {
        // return to front
        logger.info("handling " + JSON.toJSONString(storiMessage));
        // pre check
        if (validate(storiMessage)) {
            String messageId = storiMessage.getMessageId();
            String socketId = storiMessage.getSocketId();
            // String info = defaultMessageTester.parse(storiMessage.getOriginMessage());
            JSONObject request = messageStorage.getIfPresent(messageId);
            if (Objects.isNull(request)) {
                logger.error("can not found id {}'s request message in messageStorage,fail to front", messageId);
                JSONObject responseBody = new JSONObject();
                responseBody.put("code", 10003);
                responseBody.put("messageId", messageId);
                responseBody.put("reason", "request message not found.");
                WebSocketServer.sendMessage(socketId, JSON.toJSONString(responseBody));
                return;
            } else if (StringUtils.equals(request.getString("status"), "Done")) {// idempotent check
                logger.error("message {} is marked status 'Done', but received again, discard this message.", messageId);
                return;
            }
            // create response message
            JSONObject respMessage = new JSONObject();
            // put request
            respMessage.put("request", request.getJSONObject("message"));
            respMessage.put("requestHex", request.getString("requestHex"));
            respMessage.put("requestInfo", request.getString("requestInfo"));
            // put response
            JSONObject response = (JSONObject) JSON.toJSON(storiMessage.getMessageFileds());
            respMessage.put("response", response);
            respMessage.put("responseHex", storiMessage.getOriginMessage());
            // respMessage.put("responseInfo", info);
            // create response body
            JSONObject responseBody = new JSONObject();
            responseBody.put("code", 10002);
            responseBody.put("messageId", messageId);
            responseBody.put("data", respMessage);
            WebSocketServer.sendMessage(socketId, JSON.toJSONString(responseBody));
            request.put("status", "Done");
            // for idempotent
            messageStorage.invalidate(messageId);
        } else {
            logger.error("check failed message : {}", JSON.toJSONString(storiMessage));
        }
    }

    private boolean validate(StoriMessage storiMessage) {
        // todo exception handling,may store to rds.
        if (Objects.isNull(storiMessage)) {
            logger.error("Received an empty StoriMessage discard.");
            return false;
        }
        String messageId = storiMessage.getMessageId();
        if (StringUtils.isBlank(messageId)) {
            logger.error("prop messageId not found in StoriMessage discard.");
            return false;
        }
        String socketId = storiMessage.getSocketId();
        if (StringUtils.isBlank(socketId)) {
            logger.error("prop socketId not found in StoriMessage discard.");
            return false;
        }
        String originMessage = storiMessage.getOriginMessage();
        if (StringUtils.isBlank(originMessage)) {
            logger.error("prop originMessage not found in StoriMessage discard.");
            return false;
        }
        Map<Integer, String> messageFileds = storiMessage.getMessageFileds();
        if (Objects.isNull(messageFileds) || messageFileds.isEmpty()) {
            logger.error("messageFileds is empty in StoriMessage discard.");
            return false;
        }
        return true;
    }

}

