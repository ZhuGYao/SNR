package com.zgy.snr.common.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zgy
 * @data 2021/3/17 17:41
 */

public class ObservableMoreResult<T> {

    List<T> value;

    public ObservableMoreResult() {
        value = new ArrayList<>();
    }

    public void  addValue(T t){
        value.add(t);
    }

    public List<T> getValue() {
        return value;
    }

    public void setValue(List<T> value) {
        this.value = value;
    }
}
