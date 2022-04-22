package com.stori.demo.processor.service;


import com.stori.demo.processor.model.MessageResult;

public interface MessageParseService extends MessageBaseService {

    /**
     * parse header from pkt
     *
     * @return
     */
    MessageResult parseHeaderByPkt(String pkt);

    /**
     * parse header from source header
     *
     * @return
     */
    public MessageResult parseHeader(String header);

    /**
     * parse msg from pkt
     *
     * @return
     */
    public MessageResult parseMsgByPkt(String pkt);

    /**
     * parse msg by xml
     *
     * @return
     */
    public MessageResult parseMsgByXml();

    /**
     * parse msg by config
     *
     * @return
     */
    public MessageResult parseMsgByConfig(String msg);
}
