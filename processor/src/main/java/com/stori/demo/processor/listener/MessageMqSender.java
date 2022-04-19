package com.stori.demo.processor.listener;

import com.alibaba.fastjson.JSON;
import com.stori.demo.processor.constant.MessageStatus;
import com.stori.demo.processor.model.MessageLifecycle;
import com.stori.demo.processor.model.StoriMessage;
import com.stori.demo.processor.mq.producer.service.MqProducerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageMqSender {
    protected final Logger logger = LoggerFactory.getLogger(MessageMqSender.class);

    @Autowired
    private MqProducerService mqProducerService;

    public void messageSender(MessageLifecycle messageLifecycle) {
        messageLifecycle.setStatus(MessageStatus.getNextStatus(messageLifecycle.getStatus()));

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
            return;
        }

        // step 2: update status
        messageLifecycle.setStatus(MessageStatus.getNextStatus(messageLifecycle.getStatus()));
        System.out.println(JSON.toJSONString(storiMessage));
    }
}
