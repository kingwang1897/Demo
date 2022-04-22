package com.stori.demo.processor.constant;

public class Constant {
    // Header
    public static Integer MESSAGE_HEADER_LENGTH_HEX = 92;
    public static Integer MESSAGE_HEADER_LENGTH = 46;
    public static String MESSAGE_HEADER_FLAG = "12";
    public static Integer MESSAGE_HEADER_MESSAGE_LENGTH = 4;
    public static String MESSAGE_HEADER_MESSAGE_LENGTH_CONTENT = "0";
    public static String MESSAGE_HEADER_DESTINATION = "48010000000";
    public static String MESSAGE_HEADER_SOURCE = "00010000000";
    public static String MESSAGE_HEADER_RESERVED = "000000";
    public static String MESSAGE_HEADER_BATCH = "00";
    public static String MESSAGE_HEADER_TRANSACTION = "00000000";
    public static String MESSAGE_HEADER_USER = "00";
    public static String MESSAGE_HEADER_REJECT = "00000";

    // msg
    public static Integer MESSAGE_TYPE_ID_LENGTH = 8;
    public static Integer MESSAGE_TYPE_ID_LENGTH_ASCII = 4;
    public static Integer MESSAGE_BIT_MAP_LENGTH = 16;
    public static Integer MESSAGE_BIT_MAP_LENGTH_EXTEND = 32;
    public static String MESSAGE_ENCODING = "gbk";

    // Type Id
    public final static Integer MESSAGE_TYPE_ID_CODE = 16;
    public final static Integer MESSAGE_TYPE_ID_RESPONSE = 16;
    public final static String MESSAGE_TYPE_ID_MANGER = "0800";
    public final static String MESSAGE_TYPE_ID_USER = "0100";
    public final static String MESSAGE_TYPE_ID_NOTIFY = "0820";

    // respone
    public final static String MESSAGE_RESULT_SUCCESS = "00";
    public final static String MESSAGE_RESULT_FAILURE = "01";
    public final static String MESSAGE_RESPONSE_AUTH_SUCCESS = "123456";

    // field id
    public final static Integer MESSAGE_FIELD_MIN = 2;
    public final static Integer MESSAGE_FIELD_MAX = 128;
    public final static Integer MESSAGE_RESPONSE_ID = 39;
    public final static Integer MESSAGE_RESPONSE_AUTH_ID = 38;

    // lifecycle
    public final static Integer MESSAGE_CALL_INIT = 1;
    public final static Integer MESSAGE_CALL_RETRY = 3;

    // exception
    public final static Integer MESSAGE_CODE_ERROR = 999;
}
