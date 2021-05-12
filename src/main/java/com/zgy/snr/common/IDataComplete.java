package com.zgy.snr.common;

/**
 * 是否完成数据接收
 * @author zgy
 * @data 2021/3/18 11:59
 */

@FunctionalInterface
public interface IDataComplete {

    Boolean isComplete(String recv);
}
