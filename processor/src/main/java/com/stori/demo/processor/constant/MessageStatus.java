package com.stori.demo.processor.constant;

public enum MessageStatus {
    PREPARSE(0, "待解析", 0),
    PARSEING(1, "解析中", 10000),

    PREHANDLE(2, "待处理", 0),
    HANDLEING(3, "处理中", 10000),

    PRERESPONSE(4, "待响应", 0),
    RESPONSEING(5, "响应中", 10000),

    PRESEND(6, "待发送", 0),
    SENDING(7, "待发送", 10000),

    DONE(8, "完成", 0),
    FAILURE(10, "失败", 0);

    private final Integer flag;

    private final String name;

    private final Integer timeout;

    private MessageStatus(Integer flag, String name, Integer timeout) {
        this.flag = flag;
        this.name = name;
        this.timeout = timeout;
    }

    public MessageStatus getNextStatus(String name) {
        MessageStatus sreStatus = getMessageStatus(name);
        if (sreStatus == null) {
            return null;
        }

        for (MessageStatus statusEnum : MessageStatus.values()) {
            if (sreStatus.flag + 1 == statusEnum.flag) {
                return statusEnum;
            }
        }

        return null;
    }

    public static MessageStatus getNextStatus(MessageStatus sreStatus) {
        if (sreStatus == null) {
            return null;
        }

        for (MessageStatus statusEnum : MessageStatus.values()) {
            if (sreStatus.flag + 1 == statusEnum.flag) {
                return statusEnum;
            }
        }

        return null;
    }

    public static MessageStatus getPreStatus(MessageStatus sreStatus) {
        if (sreStatus == null) {
            return null;
        }

        for (MessageStatus statusEnum : MessageStatus.values()) {
            if (sreStatus.flag - 1 == statusEnum.flag) {
                return statusEnum;
            }
        }

        return null;
    }

    public static MessageStatus getMessageStatus(String name) {
        if (name == null || name.isEmpty()) {
            return null;
        }

        for (MessageStatus statusEnum : MessageStatus.values()) {
            if (name.equals(statusEnum.name)) {
                return statusEnum;
            }
        }

        return null;
    }

    public Integer getFlag() {
        return flag;
    }

    public String getName() {
        return name;
    }

    public Integer getTimeout() {
        return timeout;
    }
}
