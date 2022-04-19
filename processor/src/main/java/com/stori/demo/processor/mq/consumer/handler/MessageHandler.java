package com.stori.demo.processor.mq.consumer.handler;

import com.stori.demo.processor.mq.consumer.service.impl.AbstractConsumerServiceImpl;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 请求报文报文处理
 */
public class MessageHandler extends AbstractConsumerServiceImpl {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageHandler.class);

    /**
     * 消息处理
     */
    @Override
    public ConsumeConcurrentlyStatus handle(String msg) {
        LOGGER.info("mq消息处理开始, message:{}", msg);
        try {
            // @王凯 补充消息解析、校验、响应报文生成

            // 发送消息
        } catch (Exception e) {
            LOGGER.error("处理失败:{}", e);
            return ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }

        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
}
