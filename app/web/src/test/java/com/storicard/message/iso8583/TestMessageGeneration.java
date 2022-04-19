package com.storicard.message.iso8583;

import com.alipay.sofa.util.MessageTester;
import com.solab.iso8583.IsoMessage;
import com.solab.iso8583.MessageFactory;
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
    public void generateRequestMessage() {
        IsoMessage msg = mf.newMessage(0x100);
        String hexString = msg.debugString();
        logger.info("HEX:{}", hexString);
        logger.info("ASC1:{}", toCUPSMessageString(hexString));
        String asciiString = msg.toCupsAsciiHexMessageWithOutHeader();
        logger.info("ASC2:{}", asciiString);
        mt.parse(asciiString);
    }

    @Test
    public void generateResponseMessage() {
        IsoMessage msg = mf.newMessage(0x110);
        String hexString = msg.debugString();
        logger.info("HEX:{}", hexString);
        String asciiString = msg.toCupsAsciiHexMessageWithOutHeader();
        mt.parse(asciiString);
    }

    @Test
    public void generateAscii0110() {
        IsoMessage msg = mf.newMessage(0x110);
        String hexString = msg.debugString();
        logger.info("HEX:{}", hexString);
        logger.info("ASC:{}", toCUPSMessageString(hexString));
        String asciiString = msg.toCupsAsciiHexMessageWithOutHeader();
        mt.parse(asciiString);
    }

    @Test
    public void generateKingsMessage() throws Exception {
        com.alipay.sofa.DemoTest.genarateMessage();
        mt.parse("303431333134303030303438303735323531324e4b323344333643343434333935313946313336454532303639393938443236313235363836333634433233443336433434343339353139463133364545323036393939384432363132353638363336344332334433364334343433393531394631333645453230363939393844323631323536383633363443323344333643343434333935313946313336454532303639393938443236313235363836333634433233443336433434343339353139463133364545323036393939384432363132353638363336344332334433364334343433393531394631333645453230363939393844323631323536383633363443323344333643343434333935313946313336454532303639393938443236313235363836333634433233443336433434343339353139463133364545323036393939384432363132353638363336344332334433364334343433393531394631333645453230363939393844323631323536383633363443323344333643343434333935313946313336454532303639393938443236313235363836333634433233443336433434343339353139463133364545323036393939384432363132353638363336344332334433364334343433393531394631333645453230363939393844323631323536383633363443323344333643343434333935313946313336454532303639393938443231313630303030303030303030303030303130313030303030303030303839323031303030304142434445464748");
    }

    @Test
    public void testPlainHexString() throws Exception {
        mt.parse(toCUPSMessageString("0200302004c020c0981100000000000000000100013302100012376225885741255749d0000101296490017105603532303739303030303031333332303630313230303033313536e6b52efda2c3f13726000000000000000014220006060006013242414635334638"));
    }

    @Test
    public void testPlainHexString2() throws Exception {
        mt.parse(toCUPSMessageString("02004c020c0981100000000000000000100013302100012376225885741255749d0000101296490017105603532303739303030303031333332303630313230303033313536e6b52efda2c3f13726000000000000000014220006060006013242414635334638"));
    }

    @Deprecated
    public static String toCUPSMessageString(String str) {
        char[] chars = str.toCharArray();
        StringBuffer hex = new StringBuffer();
        int bitmapStartIndex = 4;
        int bitmapEndIndex = bitmapStartIndex + 16;
        for (int i = 0; i < chars.length; i++) {
            if (i < bitmapStartIndex || i >= bitmapEndIndex) {
                hex.append(Integer.toHexString((int) chars[i]));
            } else {
                hex.append(chars[i]);
            }
        }
        return hex.toString();
    }
}
