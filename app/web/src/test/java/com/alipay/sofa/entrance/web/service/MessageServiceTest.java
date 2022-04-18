package com.alipay.sofa.entrance.web.service;

import com.alibaba.fastjson.JSON;
import com.alipay.sofa.web.test.base.AbstractTestBase;
import org.junit.Test;

import javax.annotation.Resource;
import java.io.IOException;

public class MessageServiceTest extends AbstractTestBase {

    @Resource
    private MessageService messageService;

    @Test
    public void testSofaRestGet() throws IOException {
        System.out.println(JSON.toJSONString(messageService.loadTemplatesFromResource(), true));
    }

    @Override
    public void childSetUp() {

    }
}