package com.zgy.snr.common.check;

import com.zgy.snr.common.ICheckFormat;
import org.springframework.stereotype.Component;

/**
 * 设备接口板下发验证
 * @author zgy
 * @data 2021/3/30 11:02
 */

@Component
public class BoardCheckFormat implements ICheckFormat {
    @Override
    public Boolean checkFormat(String recv) {
        return true;
    }

    @Override
    public String substring(String recv) {
        return recv;
    }
}
