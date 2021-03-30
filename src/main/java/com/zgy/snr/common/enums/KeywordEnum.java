package com.zgy.snr.common.enums;

/**
 * 项目所需关键词
 * @author zgy
 * @data 2021/3/17 14:50
 */

public enum KeywordEnum {

    CHANNEL_HEAD("5F5F", "接口板包头"),
    CHANNEL_TAIL("7A7A", "接口板包尾"),
    CORE_HEAD("E1", "机芯包头"),
    CORE_TAIL("EC", "机芯包尾"),
    CORE_HEARTBEAT("6D6D", "机芯心跳"),
    CORE_A("06", "A机芯"),
    CORE_B("08", "B机芯"),
    CORE_C("20", "C机芯"),
    CHECK_OR_OCCUPANCY("FF", "校验位及关键字占位");



    public final String value;
    public final String type;

    KeywordEnum(String value, String type) {
        this.type = type;
        this.value = value;
    }
}
