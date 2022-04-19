package com.stori.demo.processor;

import org.apache.rocketmq.spring.autoconfigure.RocketMQAutoConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = {"com.stori.demo.processor"}, exclude = {RocketMQAutoConfiguration.class})
@ImportResource({"classpath*:META-INF/web-app/*.xml"})
@EnableScheduling
public class ProcessorApplication {

    private static final Logger logger = LoggerFactory.getLogger(ProcessorApplication.class);

    public static void main(String[] args){
        SpringApplication springApplication = new SpringApplication(ProcessorApplication.class);
        ApplicationContext applicationContext = springApplication.run(args);

        logger.info("application start");
    }

}
