package com.zgy.snr.common;

/**
 * 自定义下发校验验证
 * @author zgy
 * @data 2021/3/30 10:48
 */

public interface ICheckFormat {

    /**
     * 验证是否符合规范
     * @param recv
     * @return
     */
    Boolean checkFormat(String recv);

    /**
     * 根据规范截取所需数据
     * @param recv
     * @return
     */
    String substring(String recv);
}
