package com.zgy.snr.common.enums;

/**
 * @Desc: 是否 枚举
 */
public enum YesOrNo {
    NO(0, "否"),
    YES(1, "是");

    public final Integer value;
    public final String type;

    YesOrNo(Integer value, String type) {
        this.type = type;
        this.value = value;
    }
}
