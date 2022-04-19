package com.stori.demo.processor.mq.consumer.listener;

import com.stori.demo.processor.mq.consumer.properties.MqConsumerProperties;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AbstractConsumerListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractConsumerListener.class);

    @Autowired
    private MqConsumerProperties consumerProperties;

    /**
     * 开启消息监听
     */
    public void listener(String topic, String tags) throws MQClientException {
        LOGGER.info("开启" + topic + ": " + tags + "消息监听");

        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("");
    }
}
