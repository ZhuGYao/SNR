package com.zgy.snr.common;

/**
 * @author zgy
 * @data 2021/3/15 18:38
 */

public abstract class AbstractCheckFormat {

    /**
     * 校验是否符合自己的规则
     * @param msg 待检验字符串
     * @return 是否符合
     */
    protected abstract boolean checkFormat(String msg);

}
