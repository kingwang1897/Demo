package com.alipay.sofa.web.test.base;

import com.alipay.sofa.SOFABootWebSpringApplication;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Referenced document: http://docs.spring.io/spring-boot/docs/1.4.2.RELEASE/reference/htmlsingle/#boot-features-testing
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SOFABootWebSpringApplication.class)
public abstract class AbstractTestBaseNoWeb {

    @Before
    public void setUp() {
        childSetUp();
    }

    /**
     * 测试子类每个方法执行前需要进行的初始化代码放在此
     */
    public abstract void childSetUp();

}
