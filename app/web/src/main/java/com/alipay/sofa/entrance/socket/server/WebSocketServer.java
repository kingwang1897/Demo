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
        logger.info("connect:" + socketId + ",online:" + getOnlineCount());
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
        logger.info("closed:" + socketId + ", online:" + getOnlineCount());
    }


    @OnMessage
    public void onMessage(String message, Session session) {
        logger.info("from:" + socketId + ",message:" + message);
        //save to rds or redis
        try {
            if (StringUtils.isNotBlank(message)) {
                JSONObject request = JSON.parseObject(message);
                Integer code = request.getInteger("code");
                if (code == 10001) {// transaction message
                    webSocketMessageHandler.handleRequest(socketId, request);
                }
            } else {
                logger.warn("received empty message from socketId:{}", socketId);
            }
        } catch (Exception e) {
            logger.warn("error on handle " + socketId + " " + message, e);
        }
    }


    @OnError
    public void onError(Session session, Throwable error) {
        logger.error("error:" + this.socketId + ", reason:" + error.getMessage());
        error.printStackTrace();
    }


    public void sendMessage(String message) {
        try {
            this.session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void sendMessage(String socketId, String message) {
        try {
            if (webSocketMap.containsKey(socketId)) {
                webSocketMap.get(socketId).sendMessage(message);
            } else {
                logger.error("can not found websocket named {}", socketId);
            }
        } catch (Exception e) {
            logger.error(String.format("error occur when send %s %s", socketId, message), e);
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


