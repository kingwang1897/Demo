package com.stori.demo.processor.mq.consumer.enums;

/**
 * 消息处理器枚举
 */
public enum MqConsumerBeanEnum {
    /**
     * 报文解析handler
     */
    MESSAGE_HANDLER("REQUEST_QUEUE:tag", "messageHandler"),;

    private String topicTag;

    private String beanName;

    public String getTopicTag() {
        return topicTag;
    }

    public void setTopicTag(String topicTag) {
        this.topicTag = topicTag;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    MqConsumerBeanEnum(String topicTag, String beanName) {
        this.topicTag = topicTag;
        this.beanName = beanName;
    }

    public static MqConsumerBeanEnum getBeanByTopicTag(String topicTag) {
        for (MqConsumerBeanEnum beanEnum : MqConsumerBeanEnum.values()) {
            if (beanEnum.getTopicTag().equals(topicTag)) {
                return beanEnum;
            }
        }
        return null;
    }
}
