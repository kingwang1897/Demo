package com.stori.demo.processor.impl;


import com.alibaba.fastjson.JSON;
import com.stori.demo.processor.constant.Constant;
import com.stori.demo.processor.constant.MessageStatus;
import com.stori.demo.processor.model.MessageLifecycle;
import com.stori.demo.processor.model.StoriMessage;
import com.stori.demo.processor.mq.producer.service.MqProducerService;
import com.stori.demo.processor.service.MessageBaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("messageSendService")
public class MessageSendServiceImpl implements MessageBaseService {
    protected final Logger logger = LoggerFactory.getLogger(MessageParseServiceImpl.class);

    @Autowired
    private MqProducerService mqProducerService;

    /**
     * send pkt(contain header and msg)
     *
     * @return
     */
    @Override
    public MessageLifecycle execute(MessageLifecycle messageLifecycle) {
        if (messageLifecycle.getStatus().equals(MessageStatus.SENDING)) {
            return messageLifecycle;
        }
        messageLifecycle.setCallCount(messageLifecycle.getCallCount() + Constant.MESSAGE_CALL_INIT);
        messageLifecycle.setStatus(MessageStatus.getNextStatus(messageLifecycle.getStatus()));
        messageLifecycle.setMessageProcessorTime(System.currentTimeMillis());

        // step 1: send message to message
        StoriMessage storiMessage = new StoriMessage();
        storiMessage.setMessageChannel(messageLifecycle.getMessageId());
        storiMessage.setMessageId(messageLifecycle.getMessageId());
        storiMessage.setSocketId(messageLifecycle.getSocketId());
        storiMessage.setOriginMessage(messageLifecycle.getMessageResult().getMessage());
        storiMessage.setMessageFileds(messageLifecycle.getMessageResult().getMessageFileds());
        boolean result = mqProducerService.sendMessage(JSON.toJSONString(storiMessage));
        if (!result) {
            logger.error("messageSender error, messageId is :{}.", messageLifecycle.getMessageId());
            return messageLifecycle;
        }

        // step 2: update status
        messageLifecycle.setStatus(MessageStatus.getNextStatus(messageLifecycle.getStatus()));
        messageLifecycle.setCallCount(Constant.MESSAGE_CALL_INIT);
        return messageLifecycle;
    }
}
