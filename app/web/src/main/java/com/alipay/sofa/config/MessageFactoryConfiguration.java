package com.alipay.sofa.config;

import com.solab.iso8583.IsoMessage;
import com.solab.iso8583.MessageFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageFactoryConfiguration {
    @Bean
    public MessageFactory<IsoMessage> defaultMessageFactory() {
        MessageFactory<IsoMessage> mf = new MessageFactory<>();
        mf.setCharacterEncoding("UTF-8");
        return mf;
    }
}
