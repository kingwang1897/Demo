package com.stori.demo.processor.model;

import java.util.Map;

public class MessageHandle {

    private String type;

    private Map<Integer, String> messageFileds;

    public MessageHandle(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<Integer, String> getMessageFileds() {
        return messageFileds;
    }

    public void setMessageFileds(Map<Integer, String> messageFileds) {
        this.messageFileds = messageFileds;
    }
}
