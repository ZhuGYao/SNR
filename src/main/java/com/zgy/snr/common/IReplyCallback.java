package com.zgy.snr.common;

/**
 * 消息校验接口
 * @author zgy
 * @data 2021/3/16 10:38
 */

@FunctionalInterface
public interface IReplyCallback {

    Boolean recvData(String recv);
}
