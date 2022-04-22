package com.alipay.sofa.common.message.service;

import com.alipay.sofa.mq.consumer.service.MqConsumerService;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public abstract class AbstractConsumerService implements ConsumerService {

    protected Logger LOGGER = LoggerFactory.getLogger(getClass());

    /**
     * 消息前置处理
     */
    public ConsumeConcurrentlyStatus beforeHandler(String message) {
        LOGGER.info("mq消息前置处理： message:{}", message);
        return null;
    }




    /**
     * 消息后置处理
     */
    public void afterHandler(String message, Date startHandlerTime, ConsumeConcurrentlyStatus status) {
        LOGGER.info("mq消息后置处理： message:{}", message);
    }
}
