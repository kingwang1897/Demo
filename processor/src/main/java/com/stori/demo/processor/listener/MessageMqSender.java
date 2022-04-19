package com.stori.demo.processor.listener;

import com.alibaba.fastjson.JSON;
import com.stori.demo.processor.constant.MessageStatus;
import com.stori.demo.processor.model.MessageLifecycle;
import com.stori.demo.processor.model.StoriMessage;
import com.stori.demo.processor.mq.producer.service.MqProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageMqSender {

    @Autowired
    private MqProducerService mqProducerService;

    public void messageSender(MessageLifecycle messageLifecycle) {
        messageLifecycle.setStatus(MessageStatus.getNextStatus(messageLifecycle.getStatus()));
        // step 1: send message to message

        StoriMessage storiMessage = new StoriMessage();
        storiMessage.setMessageChannel(messageLifecycle.getMessageId());
        storiMessage.setSocketId(messageLifecycle.getSocketId());
        storiMessage.setOriginMessage(messageLifecycle.getMessageResult().getMessage());
        boolean result = mqProducerService.sendMessage(JSON.toJSONString(storiMessage));
        if (!result) {
            return;
        }

        // step 1: update status
        messageLifecycle.setStatus(MessageStatus.getNextStatus(messageLifecycle.getStatus()));
    }
}
