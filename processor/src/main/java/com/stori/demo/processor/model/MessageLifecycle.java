package com.stori.demo.processor.model;


import com.stori.demo.processor.constant.MessageStatus;

public class MessageLifecycle {

    private String messageId;

    private String socketId;

    private MessageStatus status;

    private MessageResult messageResult;

    private HelpResult helpResult;

    private Long messageProcessorTime;

    public MessageLifecycle(String socketId, String messageId, String message) {
        this.socketId = socketId;
        this.messageId = messageId;
        MessageResult messageResult = new MessageResult();
        messageResult.setPkt(message);
        this.messageResult = messageResult;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public MessageStatus getStatus() {
        return status;
    }

    public void setStatus(MessageStatus status) {
        this.status = status;
    }

    public MessageResult getMessageResult() {
        return messageResult;
    }

    public void setMessageResult(MessageResult messageResult) {
        this.messageResult = messageResult;
    }

    public String getSocketId() {
        return socketId;
    }

    public void setSocketId(String socketId) {
        this.socketId = socketId;
    }

    public Long getMessageProcessorTime() {
        return messageProcessorTime;
    }

    public void setMessageProcessorTime(Long messageProcessorTime) {
        this.messageProcessorTime = messageProcessorTime;
    }

    public HelpResult getHelpResult() {
        return helpResult;
    }

    public void setHelpResult(HelpResult helpResult) {
        this.helpResult = helpResult;
    }
}
