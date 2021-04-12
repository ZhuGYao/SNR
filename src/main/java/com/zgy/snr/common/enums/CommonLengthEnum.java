package com.zgy.snr.common.enums;

/**
 * 通用长度，便于截取数据，方便统一维护
 * @author zgy
 * @data 2021/3/18 16:59
 */

public enum CommonLengthEnum {

    CORE_A_HEAD_LENGTH(4, "A机芯包头以及长度字节共占4位"),
    CORE_B_HEAD_LENGTH(6, "B机芯包头以及长度字节共占4位"),
    CORE_C_HEAD_LENGTH(8, "C机芯包头以及长度字节共占4位"),
    A_HEART_LENGTH(20, "A心跳包长度"),
    B_HEART_LENGTH(22, "B心跳包长度"),
    C_HEART_LENGTH(24, "C心跳包长度"),
    COMPLETE(4, "满足四条完成");

    public final Integer value;
    public final String type;

    CommonLengthEnum(Integer value, String type) {
        this.type = type;
        this.value = value;
    }
}
