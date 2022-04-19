package com.stori.demo.processor.mq.consumer.service.impl;

import com.stori.demo.processor.mq.consumer.service.MqConsumerService;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class AbstractConsumerServiceImpl implements MqConsumerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractConsumerServiceImpl.class);

    /**
     * 消息前置处理
     */
    public ConsumeConcurrentlyStatus beforeHandler(String message) {
        LOGGER.info("mq消息前置处理： message:{}", message);
        return null;
    }

    /**
     * 消息处理
     */
    public ConsumeConcurrentlyStatus handle(String message) {
        LOGGER.info("mq消息业务逻辑处理： message:{}", message);
        return null;
    }

    /**
     * 消息后置处理
     */
    public void afterHandler(String message, Date startHandlerTime, ConsumeConcurrentlyStatus status) {
        LOGGER.info("mq消息后置处理： message:{}", message);
    }
}
