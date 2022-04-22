package com.alipay.sofa.entrance.socket.server;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.sofa.entrance.socket.WebSocketMessageHandler;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * websocket server
 */
@Component
@ServerEndpoint("/request/{socketId}")
public class WebSocketServer {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketServer.class);

    public static WebSocketMessageHandler webSocketMessageHandler;

    private static int onlineCount = 0;

    private static ConcurrentHashMap<String, WebSocketServer> webSocketMap = new ConcurrentHashMap<>();

    private Session session;

    private String socketId = "";

    @OnOpen
    public void onOpen(Session session, @PathParam("socketId") String socketId) {
        this.session = session;
        this.socketId = socketId;
        if (webSocketMap.containsKey(socketId)) {
            try {
                webSocketMap.get(socketId).session.close();
            } catch (IOException e) {
                logger.warn("error on close " + socketId, e);
                // should be silent
            }
            webSocketMap.remove(socketId);
        } else {
            addOnlineCount();
        }
        webSocketMap.put(socketId, this);
        logger.info("WebSocket connect:" + socketId + ",online:" + getOnlineCount());
        JSONObject notice = new JSONObject();
        notice.put("code", 10000);
        sendMessage(notice.toJSONString());
    }


    @OnClose
    public void onClose() {
        if (webSocketMap.containsKey(socketId)) {
            webSocketMap.remove(socketId);
            subOnlineCount();
        }
        logger.info("WebSocket closed:" + socketId + ", online:" + getOnlineCount());
    }


    @OnMessage
    public void onMessage(String message, Session session) {
        logger.info("WebSocket received from:" + socketId + ", message:" + message);
        //save to rds or redis
        try {
            if (StringUtils.isNotBlank(message)) {
                JSONObject request = JSON.parseObject(message);
                Integer code = request.getInteger("code");
                if (code == 10000) {
                    logger.info("WebSocket received welcome message, code:{} message", code, message);
                    return;
                }
                if (code == 10001) {// transaction message
                    logger.info("WebSocket handling transaction message, code:{} message", code, message);
                    webSocketMessageHandler.handleRequest(socketId, request);
                    logger.info("WebSocket handled  transaction message, code:{} message", code, message);
                    return;
                }
                logger.warn("WebSocket received unhandled message, code:{} message", code, message);
            } else {
                logger.warn("WebSocket received empty message from socketId:{}", socketId);
            }
        } catch (Exception e) {
            logger.warn("WebSocket Error on handle " + socketId + " " + message, e);
        }
    }


    @OnError
    public void onError(Session session, Throwable error) {
        logger.error("WebSocket error:" + this.socketId + ", reason:" + error.getMessage(), error);
    }


    private void sendMessage(String message) {
        try {
            synchronized (this.session) {
                this.session.getBasicRemote().sendText(message);
            }
        } catch (IOException e) {
            logger.error(String.format("WebSocket Error occur when send %s %s", socketId, message), e);
        }
    }

    public static void sendMessage(String socketId, String message) {
        try {
            if (webSocketMap.containsKey(socketId)) {
                logger.info("WebSocket send Message  {}:{}", socketId, message);
                webSocketMap.get(socketId).sendMessage(message);
                logger.info("WebSocket send Done     {}:{}", socketId, message);
            } else {
                logger.error("Can not found websocket named {}", socketId);
            }
        } catch (Exception e) {
            logger.error(String.format("WebSocket Error occur when send %s %s", socketId, message), e);
        }
    }


    public static synchronized int getOnlineCount() {
        return onlineCount;
    }


    public static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }


    public static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }

}


