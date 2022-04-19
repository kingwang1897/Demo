package com.alipay.sofa.entrance.web.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.sofa.util.MessageTester;
import com.alipay.sofa.web.test.base.AbstractTestBase;
import org.junit.Test;

import javax.annotation.Resource;

public class MessageServiceTest extends AbstractTestBase {

    @Resource
    private MessageService messageService;

    private JSONObject request = JSONObject.parseObject("{\"0\":\"0100\",\"2\":\"4761739001010119\",\"3\":\"000000\",\"4\":\"000000012300\",\"7\":\"0304054133\",\"11\":\"001205\",\"12\":\"001205\",\"13\":\"0205\",\"14\":\"2705\",\"15\":\"0205\",\"18\":\"5399\",\"19\":\"086\",\"25\":\"08\",\"32\":\"10001\",\"33\":\"10002\",\"37\":\"206305000014\",\"41\":\"29110001\",\"42\":\"100101001010010\",\"49\":\"840\",\"100\":\"62280000\"}");

    private MessageTester mt = MessageTester.newBuilder().url("https://iso8583.info/lib/CUP/Online/msg").build();

    @Override
    public void childSetUp() {

    }

    @Test
    public void of() {
        String hex = messageService.of(request).toCupsAsciiHexMessageWithOutHeader();
        System.out.println(hex);
        mt.parse(hex);
    }

    @Test
    public void loadTemplatesFromResource() {
        System.out.println(JSON.toJSONString(messageService.loadTemplatesFromResource(), true));
    }
}