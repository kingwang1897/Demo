package com.alipay.sofa;

import com.alipay.sofa.rpc.common.json.JSON;
import com.solab.iso8583.IsoMessage;
import com.solab.iso8583.IsoType;
import com.solab.iso8583.MessageFactory;
import com.solab.iso8583.impl.SimpleTraceGenerator;
import com.solab.iso8583.parse.ConfigParser;

public class Test {
    private static int hexToInt(int n) {
        return Integer.parseInt(String.valueOf(n), 16);
    }

    public static void main(String[] args) throws Exception {
        MessageFactory<IsoMessage> mf = new MessageFactory<IsoMessage>();
        mf.setBinaryHeader(true);
        mf.setBinaryFields(false);
        mf.setUseBinaryBitmap(false);
        mf.setForceStringEncoding(true);
        mf.setUseBinaryMessages(false);

        mf.setAssignDate(true);
        mf.setTraceNumberGenerator(new SimpleTraceGenerator((int) (System.currentTimeMillis() % 100000)));
        System.out.println("NEW MESSAGE");

        IsoMessage m = mf.newMessage(hexToInt(800));
        m.setIsoHeader("46013036303434383031303030303030303030303130303030303030000000003030303030303030003030303030");
        m.setValue(7, "0413140000", IsoType.DATE10, 10);
        m.setValue(11, "480752", IsoType.NUMERIC, 6);
        m.setValue(48, "4e4b313233343536373839306162636465663132333435363738393061626364656631323334353637383930616263646566313233343536373839306162636465663132333435363738393061626364656631323334353637383930616263646566313233343536373839306162636465663132333435363738393061626364656631323334353637383930616263646566313233343536373839306162636465663132333435363738393061626364656631323334353637383930616263646566313233343536373839306162636465663132333435363738393061626364656631323334353637383930616263646566313233343536373839306162636130383030822000000001080004000001100000003034313231343031363030383030822000000001080004000001100000003034313231343030303030383030822000000001080004000001100000003034313231343030303030383030822000000001080004000001100000003034313231343030303030383030822000000001080004000001100000003034313231343030303030383030822000000001080004000001100000003034313231343030303030383030822000000001080004000001100000003034313231343030303030383030822000000001080004000001100000003034313231343030303030383030323134303231343032313430", IsoType.LLLLBIN, 1024);
        m.setValue(53, "1600000000000000", IsoType.NUMERIC, 16);
        m.setValue(70, "101", IsoType.NUMERIC, 3);
        m.setValue(96, "0000000000000000", IsoType.ALPHA, 16);
        m.setValue(100, "92010000", IsoType.LLVAR, 11);
        m.setValue(128, "4346414646314336", IsoType.ALPHA, 16);

        m.setForceSecondaryBitmap(true);
        System.out.println(m.debugString());
        print(m);


        //初始化获取解析配置文件j8583.xml
        MessageFactory messageFactory = ConfigParser.createDefault();
        //与组包一致
        messageFactory.setBinaryHeader(true);
        messageFactory.setBinaryFields(false);
        messageFactory.setUseBinaryBitmap(false);
        messageFactory.setForceStringEncoding(true);
        messageFactory.setUseBinaryMessages(false);

        //解析报文
        IsoMessage isoMessage = messageFactory.parseMessage(m.debugString().getBytes(), 0, true);
        print(isoMessage);
    }

    // 输出一个报文内容
    private static void printIn(IsoMessage m) {
        System.out.println("----------------------------------------------------- ");
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(m.getIsoHeader());
        stringBuffer.append(Integer.toHexString(m.getType()));
        System.out.println("Message Header = [" + m.getIsoHeader() + "]");
        System.out.println("Message TypeID = [" + m.getType() + "]");
        for (int i = 2; i <= 128; i++) {
            if (m.hasField(i)) {
                System.out.println("FieldID: " + i
                        + " <" + m.getField(i).getType()
                        + ">\t[" + JSON.toJSONString(m.getObjectValue(i))
                        + "]\t[" + m.getField(i).toString() + "]");
            }
        }
    }

    // 输出一个报文内容
    private static void print(IsoMessage m) {
        System.out.println("----------------------------------------------------- ");
        System.out.println("Message Header = [" + m.getIsoHeader() + "]");
        System.out.println("Message TypeID = [" + m.getType() + "]");
        for (int i = 2; i <= 128; i++) {
            if (m.hasField(i)) {
                System.out.println("FieldID: " + i
                        + " <" + m.getField(i).getType()
                        + ">\t[" + JSON.toJSONString(m.getObjectValue(i))
                        + "]\t[" + m.getField(i).toString() + "]");
            }
        }
    }

    public static String stringToAscii(String value) {
        StringBuffer sbu = new StringBuffer();
        char[] chars = value.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if(i != chars.length - 1) {
                sbu.append((int)chars[i]).append(",");
            } else {
                sbu.append((int)chars[i]);
            }
        }
        return sbu.toString();
    }

}