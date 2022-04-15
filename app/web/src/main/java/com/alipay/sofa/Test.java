package com.alipay.sofa;

import com.alipay.sofa.constant.Constant;
import com.alipay.sofa.rpc.common.json.JSON;
import com.alipay.sofa.util.CommonUtil;
import com.solab.iso8583.IsoMessage;
import com.solab.iso8583.IsoType;
import com.solab.iso8583.MessageFactory;
import com.solab.iso8583.parse.ConfigParser;

public class Test {
    public static void main(String[] args) throws Exception {
        // genarate iso8583 message
        String message = genarateMessage();
        System.out.println("genarateMessage result is:" + message);

        // parse iso8583 message
        parseMessage(message);
    }

    public static String genarateMessage() throws Exception {
        MessageFactory<IsoMessage> mf = new MessageFactory<IsoMessage>();
        mf.setBinaryHeader(true);
        mf.setBinaryFields(false);
        mf.setUseBinaryBitmap(false);
        mf.setForceStringEncoding(true);
        mf.setUseBinaryMessages(false);
        mf.setAssignDate(true);
        System.out.println("NEW MESSAGE");

        IsoMessage m = mf.newMessage(Integer.parseInt("800", 16));
        m.setValue(7, "0413140000", IsoType.DATE10, 10);
        m.setValue(11, "480752", IsoType.NUMERIC, 6);
        m.setValue(48, "NK23D36C44439519F136EE2069998D26125686364C23D36C44439519F136EE2069998D26125686364C23D36C44439519F136EE2069998D26125686364C23D36C44439519F136EE2069998D26125686364C23D36C44439519F136EE2069998D26125686364C23D36C44439519F136EE2069998D26125686364C23D36C44439519F136EE2069998D26125686364C23D36C44439519F136EE2069998D26125686364C23D36C44439519F136EE2069998D26125686364C23D36C44439519F136EE2069998D26125686364C23D36C44439519F136EE2069998D26125686364C23D36C44439519F136EE2069998D26125686364C23D36C44439519F136EE2069998D21", IsoType.LLLBIN, 512);
        m.setValue(53, "1600000000000000", IsoType.NUMERIC, 16);
        m.setValue(70, "101", IsoType.NUMERIC, 3);
        m.setValue(96, "00000000", IsoType.ALPHA, 8);
        m.setValue(100, "92010000", IsoType.LLVAR, 11);
        m.setValue(128, "ABCDEFGH", IsoType.ALPHA, 8);

        String messageHeader = messageHeader(m.debugString().length() + 46);
        m.setIsoHeader(messageHeader);

        m.setForceSecondaryBitmap(true);
        System.out.println(m.debugString());

        // 报文头
        System.out.println("----IsoHeader---");
        Integer headerIndex = m.getIsoHeader().length();
        System.out.println(messageHeader);
        // 报文业务类型
        System.out.println("----IsoTypeID---");
        String messageTypeID = CommonUtil.convertStringToHex(m.debugString().substring(headerIndex, headerIndex + 4));
        System.out.println(messageTypeID);
        // 报文内容
        System.out.println("----IsoBitMap---");
        Integer bitMapLength = m.isBinaryHeader() || m.isBinaryHeader() ? 16 : 32;
        String messageBitMap = m.debugString().substring(headerIndex + 4, headerIndex + 4 + bitMapLength);
        System.out.println(messageBitMap);
        //
        System.out.println("----IsoData---");
        Integer messageIndex = headerIndex + 4 + bitMapLength;
        String messageDataResouce = m.debugString().substring(messageIndex);
        String messageData = CommonUtil.convertStringToHex(messageDataResouce);
        System.out.println(messageData);

        System.out.println("----ALL(HEX)---");
        String message = messageHeader + messageTypeID + messageBitMap + messageData;
        System.out.println(message);
        return message;
    }

    public static void parseMessage(String message) throws Exception{
        //初始化获取解析配置文件j8583.xml
        MessageFactory messageFactory = ConfigParser.createDefault();
        //与组包一致
        messageFactory.setBinaryHeader(true);
        messageFactory.setBinaryFields(false);
        messageFactory.setUseBinaryBitmap(false);
        messageFactory.setForceStringEncoding(true);
        messageFactory.setUseBinaryMessages(false);

        String messageData = CommonUtil.convertHexToString(message.substring(92 + 8 + 32));
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(message.substring(0, 92))
                .append(CommonUtil.convertHexToString(message.substring(92, 92 + 8)))
                .append(message.substring(92 + 8, 92 + 8 +  32))
                .append(messageData);
        System.out.println(stringBuffer.toString());

        //解析报文
        IsoMessage isoMessage = messageFactory.parseMessage(stringBuffer.toString().getBytes(), 92, true);
        print(isoMessage);
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

    public static String messageHeader(Integer messagelength){
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(Integer.toHexString(Constant.MESSAGE_HEALDER_LENGTH))
                        .append(CommonUtil.convertInergerToHex(Constant.MESSAGE_HEALDER_FLAG))
                        .append(CommonUtil.convertStringToHex(CommonUtil.addContentBylength(messagelength.toString(), Constant.MESSAGE_HEALDER_MESSAGE_LENGTH, Constant.MESSAGE_HEALDER_MESSAGE_LENGTH_CONTENT)))
                        .append(CommonUtil.convertStringToHex(Constant.MESSAGE_HEALDER_DESTINATION))
                        .append(CommonUtil.convertStringToHex(Constant.MESSAGE_HEALDER_SOURCE))
                        .append(CommonUtil.convertInergerToHex(Constant.MESSAGE_HEALDER_RESERVED))
                        .append(CommonUtil.convertInergerToHex(Constant.MESSAGE_HEALDER_BATCH))
                        .append(CommonUtil.convertStringToHex(Constant.MESSAGE_HEALDER_TRANSACTION))
                        .append(CommonUtil.convertInergerToHex(Constant.MESSAGE_HEALDER_USER))
                        .append(CommonUtil.convertStringToHex(Constant.MESSAGE_HEALDER_REJECT));

        return stringBuffer.toString();
    }
}