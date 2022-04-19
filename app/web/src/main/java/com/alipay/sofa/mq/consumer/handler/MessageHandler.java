package com.alipay.sofa.mq.consumer.handler;

import com.alibaba.fastjson.JSON;
import com.alipay.sofa.entrance.socket.WebSocketMessageHandler;
import com.alipay.sofa.model.StoriMessage;
import com.alipay.sofa.mq.consumer.service.impl.AbstractConsumerServiceImpl;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 请求报文报文处理
 */
@Component
public class MessageHandler extends AbstractConsumerServiceImpl {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageHandler.class);

    @Autowired
    private WebSocketMessageHandler webSocketMessageHandler;
    /**
     * 消息处理
     */
    @Override
    public ConsumeConcurrentlyStatus handle(String msg) {
        LOGGER.info("mq消息处理开始, message:{}", msg);
        try {
            // 反json序列号请求报文
            StoriMessage storiMessage = JSON.parseObject(msg, StoriMessage.class);

            webSocketMessageHandler.handleResponse(storiMessage);
        } catch (Exception e) {
            LOGGER.error("处理失败:{}", e);
            return ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }

        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
}
