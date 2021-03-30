package com.zgy.snr.service;

import com.zgy.snr.common.utils.CommonResultUtils;

/**
 * @author zgy
 * @data 2021/3/30 16:37
 */

public interface ProtocolService {

    /**
     * 读取时间
     * @return
     */
    CommonResultUtils readTime();

    /**
     * 读取版本
     * @return
     */
    CommonResultUtils readVersion();
}
