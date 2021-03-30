package com.zgy.snr.common.utils;

/**
 * @author zgy
 * @data 2021/3/17 17:41
 */

public class ObservableResult<T> {

    T value;

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
