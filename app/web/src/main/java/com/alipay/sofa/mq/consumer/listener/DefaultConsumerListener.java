package com.alipay.sofa.mq.consumer.listener;

import com.alipay.sofa.common.message.Topics;
import com.alipay.sofa.common.message.service.ConsumerService;
import com.alipay.sofa.common.message.service.ConsumerServiceProvider;
import com.alipay.sofa.mq.consumer.enums.MqConsumerBeanEnum;
import com.alipay.sofa.mq.consumer.properties.MqConsumerProperties;
import com.alipay.sofa.mq.consumer.service.MqConsumerService;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 默认消息监听器
 */
@Configuration
public class DefaultConsumerListener extends AbstractConsumerListener implements ApplicationContextAware, ApplicationListener<ContextRefreshedEvent> {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultConsumerListener.class);

    private ApplicationContext applicationContext;

    @Autowired
    private MqConsumerProperties mqConsumerProperties;

    @Resource
    private ConsumerServiceProvider consumerServiceProvider;

//    Try this @重阳
//    @Override
//    public ConsumeConcurrentlyStatus onMessage(List<MessageExt> msgs) {
//        for (MessageExt msg : msgs) {
//            LOGGER.info("Handling message :{}", msg);
//            String topic = msg.getTopic();
//            try {
//                ConsumerService consumerService = consumerServiceProvider.getService(Topics.valueOf(topic));
//                if (Objects.isNull(consumerService)) {
//                    LOGGER.error("Not found ConsumerService for {} ,msg:{}", topic, msg);
//                    // FIXME may be dead loop
//                    return ConsumeConcurrentlyStatus.RECONSUME_LATER;
//                }
//                String message = new String(msg.getBody());
//                consumerService.beforeHandler(message);
//                ConsumeConcurrentlyStatus concurrentlyStatus = consumerService.handle(message);
//                consumerService.afterHandler(message, new Date(), concurrentlyStatus);
//            } catch (Exception e) {
//                LOGGER.error("消息消费异常， msg:{}, e:{}", msg, e);
//                // FIXME may be dead loop
//                return ConsumeConcurrentlyStatus.RECONSUME_LATER;
//            }
//            LOGGER.info("Handled message :{}", msg);
//        }
//        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
//    }

    /**
     * 消息处理
     */
    @Override
    public ConsumeConcurrentlyStatus onMessage(List<MessageExt> msgs) {
        for (MessageExt msg : msgs) {
            String topicTag = msg.getTopic();

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
            super.listener("RESPONSE_QUEUE", mqConsumerProperties.getTags());
        } catch (MQClientException e) {
            LOGGER.error("consumer error", e);
        }
    }
}
