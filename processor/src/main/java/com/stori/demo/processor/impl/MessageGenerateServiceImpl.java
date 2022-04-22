package com.stori.demo.processor.impl;

import com.google.common.base.Throwables;
import com.solab.iso8583.IsoMessage;
import com.solab.iso8583.IsoType;
import com.solab.iso8583.MessageFactory;
import com.stori.demo.processor.constant.Constant;
import com.stori.demo.processor.constant.MessageStatus;
import com.stori.demo.processor.model.HelpResult;
import com.stori.demo.processor.model.MessageHandle;
import com.stori.demo.processor.model.MessageLifecycle;
import com.stori.demo.processor.model.MessageResult;
import com.stori.demo.processor.service.MessageGenerateService;
import com.stori.demo.processor.util.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("messageGenerateService")
public class MessageGenerateServiceImpl implements MessageGenerateService {
    protected final Logger logger = LoggerFactory.getLogger(MessageGenerateServiceImpl.class);

    @Value("${message.parse.header}")
    private boolean parseheader;

    /**
     * generate by xml
     *
     * @return
     */
    @Override
    public MessageLifecycle generateMsgByXml(MessageLifecycle messageLifecycle) {
        try {
            messageLifecycle.setCallCount(messageLifecycle.getCallCount() + Constant.MESSAGE_CALL_INIT);
            messageLifecycle.setStatus(MessageStatus.getNextStatus(messageLifecycle.getStatus()));
            messageLifecycle.setMessageProcessorTime(System.currentTimeMillis());

            // step 1: ISO8583 template
            MessageFactory<IsoMessage> mf = new MessageFactory<IsoMessage>();
            mf.setBinaryHeader(true);
            mf.setForceStringEncoding(true);
            mf.setConfigPath("j8583-templates-response.xml");
            Integer messageType = Integer.parseInt(CommonUtil.convertHexToString(messageLifecycle.getMessageResult().getType()), Constant.MESSAGE_TYPE_ID_CODE) + Constant.MESSAGE_TYPE_ID_RESPONSE;
            IsoMessage m = mf.newMessage(messageType);
            m.setIsoHeader(messageLifecycle.getMessageResult().getHeader());
            m.setForceSecondaryBitmap(true);

            // step 2: generate response message
            Map<Integer, String> messageFileds;
            for (int i = Constant.MESSAGE_FIELD_MIN; i <= Constant.MESSAGE_FIELD_MAX; i++) {
                if (m.hasField(i)) {
                    if (!m.getField(i).isNeedUpdate()) {
                        m.setValue(i, messageLifecycle.getMessageResult().getMessageFileds().get(i), m.getField(i).getType(), m.getField(i).getLength());
                        continue;
                    }

                    messageFileds = ((MessageHandle)messageLifecycle.getHelpResult().getData()).getMessageFileds();
                    if (messageFileds == null || !messageFileds.containsKey(i)) {
                        logger.error("generateMsgByXml error, cause by:{MessageHandle messageFileds is null}.");
                        continue;
                    }

                    m.setValue(i, messageFileds.get(i), m.getField(i).getType(), m.getField(i).getLength());
                }
            }

            // step 3: response message result
            messageLifecycle.setMessageResult(commonMessageGenarate(m));
            messageLifecycle.setStatus(MessageStatus.getNextStatus(messageLifecycle.getStatus()));
            messageLifecycle.setCallCount(Constant.MESSAGE_CALL_INIT);
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
        int headerLength = parseheader ? Constant.MESSAGE_HEADER_LENGTH_HEX : 0;
        MessageResult responseMessage = new MessageResult();
        responseMessage.setHeader(m.getIsoHeader());
        responseMessage.setType(CommonUtil.convertStringToHex(m.debugString().substring(m.getIsoHeader().length(), m.getIsoHeader().length() + Constant.MESSAGE_TYPE_ID_LENGTH_ASCII)));
        Integer bitMapLength = CommonUtil.judgeBitMap(m.debugString().substring(headerLength + Constant.MESSAGE_TYPE_ID_LENGTH_ASCII)) ? Constant.MESSAGE_BIT_MAP_LENGTH_EXTEND : Constant.MESSAGE_BIT_MAP_LENGTH;
        responseMessage.setBitMap(m.debugString().substring(m.getIsoHeader().length() + Constant.MESSAGE_TYPE_ID_LENGTH_ASCII,
                m.getIsoHeader().length() + Constant.MESSAGE_TYPE_ID_LENGTH_ASCII + bitMapLength));
        responseMessage.setMessageData(CommonUtil.convertStringToHex(m.debugString().substring(m.getIsoHeader().length() + Constant.MESSAGE_TYPE_ID_LENGTH_ASCII + bitMapLength)));
        responseMessage.setMessageFileds(CommonUtil.convertMap(m));
        responseMessage.getMessageFileds().put(0, m.debugString().substring(m.getIsoHeader().length(), m.getIsoHeader().length() + Constant.MESSAGE_TYPE_ID_LENGTH_ASCII));
        return responseMessage;
    }
}
