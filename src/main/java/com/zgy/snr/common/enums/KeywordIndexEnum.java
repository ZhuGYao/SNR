package com.zgy.snr.common.enums;

/**
 * 数据关键位置下标，便于截取
 * @author zgy
 * @data 2021/3/18 11:21
 */

public enum KeywordIndexEnum {

    CORE_DATA_START_INDEX(12, "机芯数据开始位置"),
    CORE_KEY_START_INDEX(10, "机芯数据开始位置"),
    CORE_DATA_END_INDEX(-8, "机芯数据结束位置"),
    CORE_DATA_START_INDEX_RESPONSE(6, "机芯关键字截取位置");

    public final Integer value;
    public final String type;

    KeywordIndexEnum(Integer value, String type) {
        this.type = type;
        this.value = value;
    }
}
