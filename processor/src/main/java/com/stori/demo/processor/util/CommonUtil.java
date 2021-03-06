package com.stori.demo.processor.util;

import com.solab.iso8583.IsoMessage;

import java.util.HashMap;
import java.util.Map;

public class CommonUtil {

    /**
     * convertStringToHex
     *
     * @param str
     * @return
     */
    public static String convertStringToHex(String str){
        char[] chars = str.toCharArray();
        StringBuffer hex = new StringBuffer();
        for(int i = 0; i < chars.length; i++){
            if (Integer.toHexString((int)chars[i]).length() == 1) {
                hex.append("0");
            }
            hex.append(Integer.toHexString((int)chars[i]));
        }

        return hex.toString();
    }

    /**
     * convertInergerToHex
     *
     * @param str
     * @return
     */
    public static String convertInergerToHex(String str){
        char[] chars = str.toCharArray();
        StringBuffer hex = new StringBuffer();
        for(int i = 0; i < chars.length; i++){
            hex.append(Integer.toHexString((int)chars[i] - 48));
        }

        return hex.toString();
    }

    /**
     * addContentBylength
     *
     * @param integer
     * @param length
     * @param content
     * @return
     */
    public static String addContentBylength(String integer, int length, String content) {
        if (length <= integer.length()) {
            return integer;
        }

        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < length - integer.length(); i++) {
            buffer.append(content);
        }
        buffer.append(integer);
        return buffer.toString();
    }

    /**
     * The conversion of 16 to ASCII
     * @other > Integer.toHexString(int) -> 10 to 16
     * @param hex
     * @return
     */
    public static String convertHexToString(String hex) {
        StringBuilder sb = new StringBuilder();
        // 564e3a322d302e312e34 split into two characters 56, 4e, 3a...
        for (int i = 0; i < hex.length() - 1; i += 2) {
            // convert the decimal to character
            sb.append((char) Integer.parseInt(hex.substring(i, (i + 2)), 16));
        }
        return sb.toString();
    }

    /**
     * judgeBitMap one(false) or two(true)
     *
     * @param bitMap
     * @return
     */
    public static boolean judgeBitMap(String bitMap) {
        if (bitMap.isEmpty()) {
            return false;
        }

        Integer bitInteger = Integer.parseInt(bitMap.substring(0, 1), 16);
        return bitInteger < 7 ? false : true;
    }

    /**
     * convertMap
     *
     * @param isoMessage
     * @return
     */
    public static Map<Integer, String> convertMap(IsoMessage isoMessage) {
        Map<Integer, String> messageMap = new HashMap<>();
        for (int i = 2; i <= 128; i++) {
            if (isoMessage.hasField(i)) {
                messageMap.put(i, isoMessage.getField(i).toString());
            }
        }

        return messageMap;
    }

    public static String getDefault(String data) {
        return data == null ? "" : data;
    }
}
