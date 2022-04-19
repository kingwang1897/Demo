package com.stori.demo.processor.mq.consumer.handler;

import com.alibaba.fastjson.JSON;
import com.stori.demo.processor.core.service.MessageProcessService;
import com.stori.demo.processor.mode.StoriMessage;
import com.stori.demo.processor.mq.consumer.service.impl.AbstractConsumerServiceImpl;
import com.stori.demo.processor.mq.producer.service.MqProducerService;
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
    private MqProducerService mqProducerService;

    @Autowired
    private MessageProcessService messageProcessService;

    /**
     * 消息处理
     */
    @Override
    public ConsumeConcurrentlyStatus handle(String msg) {
        LOGGER.info("mq消息处理开始, message:{}", msg);
        try {
            // 反json序列号请求报文
            StoriMessage storiMessage = JSON.parseObject(msg, StoriMessage.class);

            // 请求报文解析、校验、生成响应报文
            StoriMessage response = messageProcessService.createResponse(storiMessage);

            // 响应报文写入消息队列
//            boolean sendResult = mqProducerService.sendMessage(JSON.toJSONString(response));
        } catch (Exception e) {
            LOGGER.error("处理失败:{}", e);
            return ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }

        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
}
