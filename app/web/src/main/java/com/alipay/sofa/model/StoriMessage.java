package com.alipay.sofa.model;

import java.util.Map;

/**
 * 写入消息队列的，封装后的消息。后续可以考虑增加其他字段或者完全将originMessage解析
 */
public class StoriMessage {

    /**
     * 渠道
     */
    private String messageChannel;

    /**
     * socketId : match the connection from the client
     */
    private String socketId;

    /**
     * messageId : match the in and out message
     */
    private String messageId;

    /**
     * 原始请求报文
     */
    private String originMessage;

    /**
     * 报文域信息
     */
    private Map<Integer, String> messageFileds;

    public String getMessageChannel() {
        return messageChannel;
    }

    public void setMessageChannel(String messageChannel) {
        this.messageChannel = messageChannel;
    }

    public String getSocketId() {
        return socketId;
    }

    public void setSocketId(String socketId) {
        this.socketId = socketId;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getOriginMessage() {
        return originMessage;
    }

    public void setOriginMessage(String originMessage) {
        this.originMessage = originMessage;
    }

    public Map<Integer, String> getMessageFileds() {
        return messageFileds;
    }

    public void setMessageFileds(Map<Integer, String> messageFileds) {
        this.messageFileds = messageFileds;
    }
}
