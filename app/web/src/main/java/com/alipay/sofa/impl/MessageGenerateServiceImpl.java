package com.alipay.sofa.impl;

import com.alipay.sofa.constant.Constant;
import com.alipay.sofa.model.HelpResult;
import com.alipay.sofa.model.MessageResult;
import com.alipay.sofa.service.MessageGenerateService;
import com.alipay.sofa.util.CommonUtil;
import com.google.common.base.Throwables;
import com.solab.iso8583.IsoMessage;
import com.solab.iso8583.IsoType;
import com.solab.iso8583.MessageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service("messageGenerateService")
public class MessageGenerateServiceImpl implements MessageGenerateService {
    protected final Logger logger = LoggerFactory.getLogger(MessageGenerateServiceImpl.class);

    /**
     * generate by xml
     *
     * @return
     */
    @Override
    public HelpResult<MessageResult> generateMsgByXml(MessageResult requestMessage, HelpResult result) {
        try {
            MessageFactory<IsoMessage> mf = new MessageFactory<IsoMessage>();
            mf.setBinaryHeader(true);
            mf.setForceStringEncoding(true);
            mf.setConfigPath("j8583-templates-response.xml");

            Integer messageType = Integer.parseInt(CommonUtil.convertHexToString(requestMessage.getType()), 16) + 16;
            IsoMessage m = mf.newMessage(messageType);
            m.setIsoHeader(requestMessage.getHeader());
            m.setForceSecondaryBitmap(true);
            for (int i = 2; i <= 128; i++) {
                if (m.hasField(i)) {
                    m.setValue(i, requestMessage.getFields()[i], m.getField(i).getType(), m.getField(i).getLength());
                }
            }

            m.setValue(38, result.isSuccess() ? result.getData() : "0" , IsoType.ALPHA, 6);
            m.setValue(39, result.isSuccess() ? Constant.MESSAGE_RESULT_SUCCESS : Constant.MESSAGE_RESULT_FAILURE, IsoType.ALPHA, 2);

            return HelpResult.ok(commonMessageGenarate(m));
        } catch (Exception e) {
            logger.error("generateMsgByXml error,cause by: {}.", Throwables.getStackTraceAsString(e));
            return  HelpResult.fail("error.generate.msg");
        }
    }

    /**
     * generate by config
     *
     * @return
     */
    @Override
    public HelpResult<MessageResult> generateMsgByConfig(MessageResult requestMessage, HelpResult result) {
        String respone = result.isSuccess() ? Constant.MESSAGE_RESULT_SUCCESS : Constant.MESSAGE_RESULT_FAILURE;

        MessageFactory<IsoMessage> mf = new MessageFactory<IsoMessage>();
        mf.setBinaryHeader(true);
        mf.setForceStringEncoding(true);

        Integer messageType = Integer.parseInt(CommonUtil.convertHexToString(requestMessage.getType()), 16) + 16;
        IsoMessage m = mf.newMessage(messageType);
        m.setValue(7, "0413140000", IsoType.DATE10, 10);
        m.setValue(11, "480752", IsoType.NUMERIC, 6);
        m.setValue(39, respone, IsoType.ALPHA, 2);
        m.setValue(53, "1600000000000000", IsoType.NUMERIC, 16);
        m.setValue(70, "101", IsoType.NUMERIC, 3);
        m.setValue(100, "92010000", IsoType.LLVAR, 11);
        m.setValue(128, "ABCDEFGH", IsoType.ALPHA, 8);
        m.setIsoHeader(requestMessage.getHeader());
        m.setForceSecondaryBitmap(true);

        return HelpResult.ok(commonMessageGenarate(m));
    }

    private MessageResult commonMessageGenarate(IsoMessage m) {
        MessageResult responseMessage = new MessageResult();
        responseMessage.setHeader(m.getIsoHeader());
        responseMessage.setType(CommonUtil.convertStringToHex(m.debugString().substring(m.getIsoHeader().length(), m.getIsoHeader().length() + Constant.MESSAGE_TYPE_ID_LENGTH_ASCII)));
        Integer bitMapLength = CommonUtil.judgeBitMap(m.debugString().substring(Constant.MESSAGE_HEADER_LENGTH_HEX + Constant.MESSAGE_TYPE_ID_LENGTH_ASCII)) ? Constant.MESSAGE_BIT_MAP_LENGTH_EXTEND : Constant.MESSAGE_BIT_MAP_LENGTH;
        responseMessage.setBitMap(m.debugString().substring(m.getIsoHeader().length() + Constant.MESSAGE_TYPE_ID_LENGTH_ASCII,
                m.getIsoHeader().length() + Constant.MESSAGE_TYPE_ID_LENGTH_ASCII + bitMapLength));
        responseMessage.setMessageData(CommonUtil.convertStringToHex(m.debugString().substring(m.getIsoHeader().length() + Constant.MESSAGE_TYPE_ID_LENGTH_ASCII + bitMapLength)));
        return responseMessage;
    }
}
