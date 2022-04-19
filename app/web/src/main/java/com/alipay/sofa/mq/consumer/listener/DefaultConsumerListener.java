package com.alipay.sofa.mq.consumer.listener;

import com.alipay.sofa.mq.consumer.enums.MqConsumerBeanEnum;
import com.alipay.sofa.mq.consumer.service.MqConsumerService;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.Date;
import java.util.List;

/**
 * 默认消息监听器
 */
@Configuration
public class DefaultConsumerListener extends AbstractConsumerListener implements ApplicationContextAware, ApplicationListener<ContextRefreshedEvent> {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultConsumerListener.class);

    private ApplicationContext applicationContext;

    /**
     * 消息处理
     */
    @Override
    public ConsumeConcurrentlyStatus onMessage(List<MessageExt> msgs) {
        for (MessageExt msg: msgs) {
            String topicTag = msg.getTopic() + ":" + msg.getTags();
            MqConsumerBeanEnum mqConsumerBeanEnum = MqConsumerBeanEnum.getBeanByTopicTag(topicTag);
            if (null == mqConsumerBeanEnum) {
                break;
            }
            ConsumeConcurrentlyStatus concurrentlyStatus = null;

            try {
                Object serviceBean = applicationContext.getBean(mqConsumerBeanEnum.getBeanName());
                if (null == serviceBean) {
                    break;
                }

                if (serviceBean instanceof MqConsumerService) {
                    String message = new String(msg.getBody());
                    MqConsumerService consumerService = (MqConsumerService) serviceBean;

                    consumerService.beforeHandler(message);

                    concurrentlyStatus = consumerService.handle(message);

                    consumerService.afterHandler(message, new Date(), concurrentlyStatus);
                }
            } catch (Exception e) {
                LOGGER.error("消息消费异常， msg:{}, e:{}", msg, e);
            }
        }

        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 开启消息监听
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        try {
            super.listener("RESPONSE_QUEUE", "tag");
        } catch (MQClientException e) {
            LOGGER.error("consumer error");
        }
    }
}
