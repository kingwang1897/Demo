package com.alipay.sofa;

import com.alipay.sofa.endpoint.facade.SampleRestFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.concurrent.TimeUnit;

@SpringBootApplication(scanBasePackages = {"com.alipay.sofa"})
@ImportResource({"classpath*:META-INF/web-app/*.xml"})
@EnableScheduling
public class SOFABootWebSpringApplication {

    private static final Logger logger = LoggerFactory.getLogger(SOFABootWebSpringApplication.class);

    public static void main(String[] args){
        SpringApplication springApplication = new SpringApplication(SOFABootWebSpringApplication.class);
        ApplicationContext applicationContext = springApplication.run(args);

        if (logger.isInfoEnabled()){
            logger.info("application start");
        }

        ReferenceHolder referenceHolder = applicationContext.getBean(ReferenceHolder.class);

        //调用 SOFARest 服务
        final SampleRestFacade sampleRestFacade = referenceHolder.getSampleRestFacade();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        String response = sampleRestFacade.hello();
                        printMsg("Response from myserver-app.rest: " + response);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            TimeUnit.SECONDS.sleep(3);
                        } catch (InterruptedException e) {
                            //ignore
                        }
                    }

                }

            }
        }).start();

    }

    private static void printMsg(String msg) {
        System.out.println(msg);
        if (logger.isInfoEnabled()) {
            logger.info(msg);
        }
    }
}
