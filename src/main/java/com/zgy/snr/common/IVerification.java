package com.zgy.snr.common;

/**
 * 验证是否符合
 * @author zgy
 * @data 2021/3/17 11:32
 */

@FunctionalInterface
public interface IVerification {

    /**
     * 设置或者请求是否成功
     * @param data
     * @return
     */
    Boolean isSuccess(String data);

}
