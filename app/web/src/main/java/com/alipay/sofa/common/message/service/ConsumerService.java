package com.alipay.sofa.common.message.service;

import com.alipay.sofa.common.Typeable;
import com.alipay.sofa.common.message.Topics;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;

import java.util.Date;

public interface ConsumerService extends Typeable<Topics> {

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
