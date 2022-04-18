package com.alipay.sofa.impl;

import com.alipay.sofa.constant.Constant;
import com.alipay.sofa.model.MessageResult;
import com.alipay.sofa.service.MessageParseService;
import com.alipay.sofa.util.CommonUtil;
import com.google.common.base.Throwables;
import com.solab.iso8583.IsoMessage;
import com.solab.iso8583.MessageFactory;
import com.solab.iso8583.parse.ConfigParser;
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
    public MessageResult parsePkt(String pkt) {
        if (pkt.isEmpty() || pkt.length() < Constant.MESSAGE_HEADER_LENGTH_HEX + Constant.MESSAGE_TYPE_ID_LENGTH) {
                logger.warn("msg is invalid");
                // 转异常处理 todo
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
        return messageResult;
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


    public static void parseMessage(String message) throws Exception{
        //初始化获取解析配置文件j8583.xml
        MessageFactory messageFactory = ConfigParser.createDefault();
        //与组包一致
        messageFactory.setBinaryHeader(true);
        messageFactory.setBinaryFields(false);
        messageFactory.setUseBinaryBitmap(false);
        messageFactory.setForceStringEncoding(true);
        messageFactory.setUseBinaryMessages(false);

        String messageData = CommonUtil.convertHexToString(message.substring(92 + 8 + 32));
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(message.substring(0, 92))
                .append(CommonUtil.convertHexToString(message.substring(92, 92 + 8)))
                .append(message.substring(92 + 8, 92 + 8 +  32))
                .append(messageData);
        System.out.println(stringBuffer.toString());

        //解析报文
        IsoMessage isoMessage = messageFactory.parseMessage(stringBuffer.toString().getBytes(), 92, true);
//        print(isoMessage);
    }

    /**
     * parse msg by config
     *
     * @return
     */
    private MessageResult parseMsgByConfig(String msg, int headerLength) {
        try {
//            // 判断msg的位图，是1个还是2个
//            if (msg.isEmpty() || msg.length() < Constant.MESSAGE_TYPE_ID_LENGTH) {
//                logger.warn("msg is invalid");
//                // 转异常处理 todo
//            }

            MessageFactory messageFactory = ConfigParser.createFromClasspathConfig("0800.xml");
//            messageFactory.setBinaryHeader(CommonUtil.judgeBitMap(msg));
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
            logger.error("parseMsgByConfig error, cause by: {}.", Throwables.getStackTraceAsString(e));
            return null;
        }
    }
}
