package com.alipay.sofa.common.message.service;

import com.alipay.sofa.common.ServiceMapper;
import com.alipay.sofa.common.Typeable;
import com.alipay.sofa.common.message.Topics;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ConsumerServiceProvider extends ServiceMapper<Topics, ConsumerService> {

}
