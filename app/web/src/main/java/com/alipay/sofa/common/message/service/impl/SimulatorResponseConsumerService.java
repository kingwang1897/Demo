package com.alipay.sofa.common.message.service.impl;

import com.alibaba.fastjson.JSON;
import com.alipay.sofa.common.message.Topics;
import com.alipay.sofa.common.message.service.AbstractConsumerService;
import com.alipay.sofa.entrance.socket.WebSocketMessageHandler;
import com.alipay.sofa.model.StoriMessage;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 请求报文报文处理
 */
@Component
public class SimulatorResponseConsumerService extends AbstractConsumerService {

    @Resource
    private WebSocketMessageHandler webSocketMessageHandler;

    @Override
    public Topics getType() {
        return Topics.RESPONSE_QUEUE;
    }

    /**
     * 消息处理
     */
    @Override
    public ConsumeConcurrentlyStatus handle(String msg) {
        LOGGER.info("MQ消息处理开始 Topic:{}, message:{}", getType(), msg);
        try {
            // 反json序列号请求报文
            StoriMessage storiMessage = JSON.parseObject(msg, StoriMessage.class);
            webSocketMessageHandler.handleResponse(storiMessage);
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        } catch (Exception e) {
            LOGGER.error("处理失败:{}", e);
            return ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }
    }
}
