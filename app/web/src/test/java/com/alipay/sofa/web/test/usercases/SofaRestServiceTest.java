package com.alipay.sofa.web.test.usercases;

import com.alipay.sofa.web.test.base.AbstractTestBase;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SofaRestServiceTest extends AbstractTestBase {

    @Test
    public void testSofaRestGet(){
        assertNotNull(testRestTemplate);
        String restUrl = sofaRestHttpPrefix + "/sofarest/hello";

        ResponseEntity<String> response = testRestTemplate.getForEntity(restUrl, String.class);
        response.getBody();
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Hello SOFARest! times = 0", response.getBody());
    }

    @Override
    public void childSetUp() {
        // Implementation goes here
    }
}
