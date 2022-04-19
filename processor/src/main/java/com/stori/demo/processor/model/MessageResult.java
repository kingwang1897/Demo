package com.stori.demo.processor.model;

public class MessageResult {

    private String pkt;

    private String header;

    private String type;

    private String bitMap;

    private String messageData;

    private String[] fields = new String[129];

    public String getPkt() {
        return pkt;
    }

    public void setPkt(String pkt) {
        this.pkt = pkt;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBitMap() {
        return bitMap;
    }

    public void setBitMap(String bitMap) {
        this.bitMap = bitMap;
    }

    public String getMessageData() {
        return messageData;
    }

    public void setMessageData(String messageData) {
        this.messageData = messageData;
    }

    public String[] getFields() {
        return fields;
    }

    public void setFields(String[] fields) {
        this.fields = fields;
    }

    public String getMessage() {
        return header + type + bitMap + messageData;
    }
}
