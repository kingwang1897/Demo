package com.stori.demo.processor.mq.consumer.service;

import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;

import java.util.Date;

public interface MqConsumerService {

    /**
     * 前置
     */
    ConsumeConcurrentlyStatus beforeHandler(String message);

    /**
     * 消息处理
     */
    ConsumeConcurrentlyStatus handle(String message);

    /**
     * 后置
     */
    void afterHandler(String message, Date startHandlerTime, ConsumeConcurrentlyStatus status);

}
