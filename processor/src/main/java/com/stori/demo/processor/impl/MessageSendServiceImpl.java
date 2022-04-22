package com.stori.demo.processor.impl;


import com.alibaba.fastjson.JSON;
import com.stori.demo.processor.constant.MessageStatus;
import com.stori.demo.processor.model.MessageLifecycle;
import com.stori.demo.processor.model.StoriMessage;
import com.stori.demo.processor.mq.producer.service.MqProducerService;
import com.stori.demo.processor.service.MessageBaseService;
import com.stori.demo.processor.service.MessageSendService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("messageSendService")
public class MessageSendServiceImpl extends MessageBaseService implements MessageSendService {
    protected final Logger logger = LoggerFactory.getLogger(MessageParseServiceImpl.class);

    @Autowired
    private MqProducerService mqProducerService;

    /**
     * send pkt(contain header and msg)a
     *
     * @return
     */
    @Override
    public MessageLifecycle execute(MessageLifecycle messageLifecycle) {
        if (!executeStart(messageLifecycle, MessageStatus.SENDING)) {
            return messageLifecycle;
        }

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
        executeEnd(messageLifecycle, null, null);
        return messageLifecycle;
    }
}
