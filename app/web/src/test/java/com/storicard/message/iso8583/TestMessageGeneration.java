package com.storicard.message.iso8583;

import com.alipay.sofa.util.CommonUtil;
import com.solab.iso8583.IsoMessage;
import com.solab.iso8583.MessageFactory;
import com.storicard.message.MessageTester;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class TestMessageGeneration {


    private MessageFactory<IsoMessage> mf;
    private MessageTester mt;

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Before
    public void init() throws IOException {
        mf = new MessageFactory<>();
        mf.setCharacterEncoding("UTF-8");
        mf.setConfigPath("0x110.xml");
        mt = MessageTester.newBuilder().url("https://iso8583.info/lib/CUP/Online/msg").build();
    }

    @Test
    public void generateAscii0110() {
        IsoMessage msg = mf.newMessage(0x110);
        String hexString = msg.debugString();
        logger.info("HEX:{}", hexString);
        String asciiString = CommonUtil.convertStringToHex(hexString);
        logger.info("ASC:{}", asciiString);
        mt.parse(asciiString);
    }


}
