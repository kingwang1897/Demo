package com.stori.demo.processor.impl;

import com.google.common.base.Throwables;
import com.solab.iso8583.IsoMessage;
import com.solab.iso8583.MessageFactory;
import com.solab.iso8583.parse.ConfigParser;
import com.stori.demo.processor.constant.Constant;
import com.stori.demo.processor.constant.MessageStatus;
import com.stori.demo.processor.model.MessageLifecycle;
import com.stori.demo.processor.model.MessageResult;
import com.stori.demo.processor.service.MessageParseService;
import com.stori.demo.processor.util.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service("messageParseService")
public class MessageParseServiceImpl implements MessageParseService {
    protected final Logger logger = LoggerFactory.getLogger(MessageParseServiceImpl.class);

    @Value("${message.parse.header}")
    private boolean parseheader;

    /**
     * parse pkt(contain header and msg)
     *
     * @param messageLifecycle
     * @return
     */
    @Override
    public MessageLifecycle execute(MessageLifecycle messageLifecycle) {
        if (messageLifecycle.getStatus().equals(MessageStatus.PARSEING)) {
            return messageLifecycle;
        }
        messageLifecycle.setCallCount(messageLifecycle.getCallCount() + Constant.MESSAGE_CALL_INIT);
        messageLifecycle.setStatus(MessageStatus.getNextStatus(messageLifecycle.getStatus()));
        messageLifecycle.setMessageProcessorTime(System.currentTimeMillis());
        String pkt = messageLifecycle.getMessageResult().getPkt();

        // step 1: header
        int headerLength = parseheader ? Constant.MESSAGE_HEADER_LENGTH_HEX : 0;
        if (pkt.isEmpty() || pkt.length() < headerLength + Constant.MESSAGE_TYPE_ID_LENGTH) {
            logger.error("parsePkt error, case by: msg is invalid, pkt is: {}.", pkt);
            messageLifecycle.setStatus(MessageStatus.FAILURE);
        }

        // step 2: message parts
        Integer bitMapLength = CommonUtil.judgeBitMap(pkt.substring(headerLength + Constant.MESSAGE_TYPE_ID_LENGTH)) ? Constant.MESSAGE_BIT_MAP_LENGTH_EXTEND : Constant.MESSAGE_BIT_MAP_LENGTH;
        MessageResult messageResult = new MessageResult();
        messageResult.setHeader(parseheader ? pkt.substring(0, Constant.MESSAGE_HEADER_LENGTH_HEX) : "");
        messageResult.setType(pkt.substring(headerLength, headerLength + Constant.MESSAGE_TYPE_ID_LENGTH));
        messageResult.setBitMap(pkt.substring(headerLength + Constant.MESSAGE_TYPE_ID_LENGTH, headerLength + Constant.MESSAGE_TYPE_ID_LENGTH + bitMapLength));
        messageResult.setMessageData(pkt.substring(headerLength + Constant.MESSAGE_TYPE_ID_LENGTH + bitMapLength));

        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(messageResult.getHeader())
                .append(CommonUtil.convertHexToString(messageResult.getType()))
                .append(messageResult.getBitMap())
                .append(CommonUtil.convertHexToString(messageResult.getMessageData()));

        // step 3: message parse
        MessageResult parseResult = parseMsgByConfig(stringBuffer.toString(), headerLength);
        if (parseResult == null) {
            logger.error("MessageParseService excute error");
            messageLifecycle.setStatus(MessageStatus.getPreStatus(messageLifecycle.getStatus()));
            return messageLifecycle;
        }
        messageResult.setMessageFileds(parseResult.getMessageFileds());
        messageLifecycle.setMessageResult(messageResult);
        messageLifecycle.setStatus(MessageStatus.getNextStatus(messageLifecycle.getStatus()));
        messageLifecycle.setCallCount(Constant.MESSAGE_CALL_INIT);
        return messageLifecycle;
    }

    /**
     * parse msg by config
     *
     * @return
     */
    private MessageResult parseMsgByConfig(String msg, int headerLength) {
        try {
            MessageFactory messageFactory = ConfigParser.createFromClasspathConfig("j8583-templates-request.xml");
            messageFactory.setCharacterEncoding(Constant.MESSAGE_ENCODING);
            messageFactory.setForceStringEncoding(true);

            //解析报文
            IsoMessage isoMessage = messageFactory.parseMessage(msg.getBytes(), headerLength);
            MessageResult messageResult = new MessageResult();
            messageResult.setMessageFileds(CommonUtil.convertMap(isoMessage));
            return messageResult;
        } catch (Exception e) {
            logger.error("MessageParseService parseMsgByConfig error, cause by: {}.", Throwables.getStackTraceAsString(e));
            return null;
        }
    }

    /**
     * parse header from pkt
     *
     * @return
     */
    @Override
    public MessageResult parseHeaderByPkt(String pkt) {
        return null;
    }

    /**
     * parse header from source header
     *
     * @return
     */
    @Override
    public MessageResult parseHeader(String header) {
        return null;
    }

    /**
     * parse msg from pkt
     *
     * @return
     */
    @Override
    public MessageResult parseMsgByPkt(String pkt){
        return null;
    }

    /**
     * parse msg by xml
     *
     * @return
     */
    @Override
    public MessageResult parseMsgByXml() {
        return null;
    }

    /**
     * parse msg by config
     *
     * @return
     */
    @Override
    public MessageResult parseMsgByConfig(String msg) {

        return null;
    }
}
