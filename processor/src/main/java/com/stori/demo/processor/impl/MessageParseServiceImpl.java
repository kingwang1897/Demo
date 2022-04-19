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
import org.springframework.stereotype.Service;

@Service("messageParseService")
public class MessageParseServiceImpl implements MessageParseService {
    protected final Logger logger = LoggerFactory.getLogger(MessageParseServiceImpl.class);

    /**
     * parse pkt(contain header and msg)
     *
     * @return
     */
    @Override
    public MessageLifecycle parsePkt(MessageLifecycle messageLifecycle) {
        messageLifecycle.setStatus(MessageStatus.getNextStatus(messageLifecycle.getStatus()));
        String pkt = messageLifecycle.getMessageResult().getPkt();
        if (pkt.isEmpty() || pkt.length() < Constant.MESSAGE_HEADER_LENGTH_HEX + Constant.MESSAGE_TYPE_ID_LENGTH) {
            logger.warn("msg is invalid");
            // 转异常处理 todo
            messageLifecycle.setStatus(MessageStatus.FAILURE);
        }

        Integer bitMapLength = CommonUtil.judgeBitMap(pkt.substring(Constant.MESSAGE_HEADER_LENGTH_HEX + Constant.MESSAGE_TYPE_ID_LENGTH)) ? Constant.MESSAGE_BIT_MAP_LENGTH_EXTEND : Constant.MESSAGE_BIT_MAP_LENGTH;
        MessageResult messageResult = new MessageResult();
        messageResult.setHeader(pkt.substring(0, Constant.MESSAGE_HEADER_LENGTH_HEX));
        messageResult.setType(pkt.substring(Constant.MESSAGE_HEADER_LENGTH_HEX, Constant.MESSAGE_HEADER_LENGTH_HEX + Constant.MESSAGE_TYPE_ID_LENGTH));
        messageResult.setBitMap(pkt.substring(Constant.MESSAGE_HEADER_LENGTH_HEX + Constant.MESSAGE_TYPE_ID_LENGTH, Constant.MESSAGE_HEADER_LENGTH_HEX + Constant.MESSAGE_TYPE_ID_LENGTH + bitMapLength));
        messageResult.setMessageData(pkt.substring(Constant.MESSAGE_HEADER_LENGTH_HEX + Constant.MESSAGE_TYPE_ID_LENGTH + bitMapLength));

        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(messageResult.getHeader())
                .append(CommonUtil.convertHexToString(messageResult.getType()))
                .append(messageResult.getBitMap())
                .append(CommonUtil.convertHexToString(messageResult.getMessageData()));

        MessageResult parseResult = parseMsgByConfig(stringBuffer.toString(), Constant.MESSAGE_HEADER_LENGTH_HEX);
        messageResult.setFields(parseResult.getFields());
        messageLifecycle.setMessageResult(messageResult);
        messageLifecycle.setStatus(MessageStatus.getNextStatus(messageLifecycle.getStatus()));
        return messageLifecycle;
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

    /**
     * parse msg by config
     *
     * @return
     */
    private MessageResult parseMsgByConfig(String msg, int headerLength) {
        try {
            MessageFactory messageFactory = ConfigParser.createFromClasspathConfig("j8583-templates-request.xml");
            messageFactory.setCharacterEncoding("gbk");
            messageFactory.setForceStringEncoding(true);

            //解析报文
            IsoMessage isoMessage = messageFactory.parseMessage(msg.getBytes(), headerLength);
            MessageResult messageResult = new MessageResult();
            String[] fields = new String[129];
            messageResult.setFields(fields);

            for (int i = 2; i <= 128; i++) {
                if (isoMessage.hasField(i)) {
                    fields[i] = isoMessage.getField(i).toString();
                }
            }

            return messageResult;
        } catch (Exception e) {
            System.out.println(Throwables.getStackTraceAsString(e));
            logger.error("parseMsgByConfig error, cause by: {}.", Throwables.getStackTraceAsString(e));
            return null;
        }
    }
}
