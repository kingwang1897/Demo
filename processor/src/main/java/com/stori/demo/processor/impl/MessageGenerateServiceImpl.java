package com.stori.demo.processor.impl;

import com.google.common.base.Throwables;
import com.solab.iso8583.IsoMessage;
import com.solab.iso8583.IsoType;
import com.solab.iso8583.MessageFactory;
import com.stori.demo.processor.constant.Constant;
import com.stori.demo.processor.constant.MessageStatus;
import com.stori.demo.processor.model.HelpResult;
import com.stori.demo.processor.model.MessageLifecycle;
import com.stori.demo.processor.model.MessageResult;
import com.stori.demo.processor.service.MessageGenerateService;
import com.stori.demo.processor.util.CommonUtil;
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
    public MessageLifecycle generateMsgByXml(MessageLifecycle messageLifecycle) {
        try {
            messageLifecycle.setStatus(MessageStatus.getNextStatus(messageLifecycle.getStatus()));
            MessageFactory<IsoMessage> mf = new MessageFactory<IsoMessage>();
            mf.setBinaryHeader(true);
            mf.setForceStringEncoding(true);
            mf.setConfigPath("j8583-templates-response.xml");

            Integer messageType = Integer.parseInt(CommonUtil.convertHexToString(messageLifecycle.getMessageResult().getType()), 16) + 16;
            IsoMessage m = mf.newMessage(messageType);
            m.setIsoHeader(messageLifecycle.getMessageResult().getHeader());
            m.setForceSecondaryBitmap(true);
            for (int i = 2; i <= 128; i++) {
                if (m.hasField(i)) {
                    m.setValue(i, messageLifecycle.getMessageResult().getFields()[i], m.getField(i).getType(), m.getField(i).getLength());
                }
            }

            m.setValue(38, messageLifecycle.getHelpResult().isSuccess() ? messageLifecycle.getHelpResult().getData() : "0" , IsoType.ALPHA, 6);
            m.setValue(39, messageLifecycle.getHelpResult().isSuccess() ? Constant.MESSAGE_RESULT_SUCCESS : Constant.MESSAGE_RESULT_FAILURE, IsoType.ALPHA, 2);
            messageLifecycle.setMessageResult(commonMessageGenarate(m));

            messageLifecycle.setStatus(MessageStatus.getNextStatus(messageLifecycle.getStatus()));
            return messageLifecycle;
        } catch (Exception e) {
            logger.error("generateMsgByXml error,cause by: {}.", Throwables.getStackTraceAsString(e));
            messageLifecycle.setStatus(MessageStatus.getPreStatus(messageLifecycle.getStatus()));
            return  messageLifecycle;
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
