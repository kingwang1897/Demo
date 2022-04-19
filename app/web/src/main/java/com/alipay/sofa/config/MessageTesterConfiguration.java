package com.alipay.sofa.config;

import com.alipay.sofa.util.MessageTester;
import com.solab.iso8583.IsoMessage;
import com.solab.iso8583.MessageFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageTesterConfiguration {
    @Bean
    public MessageTester defaultMessageTester() {
        return MessageTester.newBuilder().url("https://iso8583.info/lib/CUP/Online/msg").build();
    }
}
