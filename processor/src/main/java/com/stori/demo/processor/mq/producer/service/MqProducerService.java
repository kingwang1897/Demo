package com.stori.demo.processor.mq.producer.service;

/**
 * 消息生产者接口
 */
public interface MqProducerService {

    /**
     * 发送消息
     *
     * @param msg 消息内容
     * @return 成功与否
     */
    boolean sendMessage(String msg);
}
