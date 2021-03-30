package com.zgy.snr.common.enums;

/**
 * 超时时间枚举类
 * @author zgy
 * @data 2021/3/17 18:49
 */

public enum  TimeoutEnum {

    COMMON_TIMEOUT(10000L, "通用超时时间"),
    A_HEARTBEAT_TIMEOUT(100000L, "A心跳超时时间"),
    B_HEARTBEAT_TIMEOUT(100000L, "B心跳超时时间"),
    C_HEARTBEAT_TIMEOUT(6000L, "C心跳超时时间"),
    NOT_TIMEOUT(-1L, "不需要超时返回");

    public final Long value;
    public final String type;

    TimeoutEnum(Long value, String type) {
        this.value = value;
        this.type = type;
    }
}
