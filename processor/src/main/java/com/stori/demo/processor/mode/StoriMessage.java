package com.stori.demo.processor.mode;

/**
 * 写入消息队列的，封装后的消息。后续可以考虑增加其他字段或者完全将originMessage解析
 */
public class StoriMessage {

    /**
     * 渠道
     */
    private String messageChannel;

    /**
     * socketId
     */
    private String socketId;

    /**
     * 原始请求报文
     */
    private String originMessage;

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

    public String getOriginMessage() {
        return originMessage;
    }

    public void setOriginMessage(String originMessage) {
        this.originMessage = originMessage;
    }
}
