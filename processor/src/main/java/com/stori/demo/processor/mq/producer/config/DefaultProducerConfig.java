package com.stori.demo.processor.mq.producer.config;

import com.stori.demo.processor.mq.producer.properties.MqProducerProperties;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * 消息生产者配置
 */
@Configuration
public class DefaultProducerConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultProducerConfig.class);

    @Autowired
    private MqProducerProperties producerProperties;

    /**
     * 构造MQ生产者
     */
    @Bean
    @Primary
    public DefaultMQProducer defaultProducer() throws MQClientException {
        DefaultMQProducer producer = new DefaultMQProducer(producerProperties.getGroup());

        producer.setNamesrvAddr(producerProperties.getNamesrvAddr());
        producer.setVipChannelEnabled(false);
        producer.setRetryTimesWhenSendAsyncFailed(0);
        producer.start();

        LOGGER.info("default producer创建成功, {}, {}", producerProperties.getNamesrvAddr(), producerProperties.getGroup());
        return producer;
    }
}
