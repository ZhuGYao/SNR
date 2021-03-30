package com.zgy.snr.common.utils;

import com.zgy.snr.common.enums.KeywordEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * 根据制式获取对应的关键字
 * @author zgy
 * @data 2021/3/18 10:19
 */

public class ProtocolKeywordUtils {

    // WCDMA_CDMA的关键字
    public final static Map<Integer, String > WCDMA_CDMA_KEYWORD = new HashMap<>();

    static {
        WCDMA_CDMA_KEYWORD.put(1, KeywordEnum.CORE_A.value);
        WCDMA_CDMA_KEYWORD.put(2, KeywordEnum.CORE_B.value);
    }

}
