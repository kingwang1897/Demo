package com.alipay.sofa.mq.producer.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * mq producer配置
 */
@Configuration
@ConfigurationProperties(prefix = "spring.rocketmq.producer")
public class MqProducerProperties {

    /**
     * namesrv地址
     */
    private String namesrvAddr;

    /**
     * group
     */
    private String group;

    public String getNamesrvAddr() {
        return namesrvAddr;
    }

    public void setNamesrvAddr(String namesrvAddr) {
        this.namesrvAddr = namesrvAddr;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
