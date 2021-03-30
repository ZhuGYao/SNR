package com.zgy.snr.common.check;

import com.zgy.snr.common.ICheckFormat;
import com.zgy.snr.common.enums.KeywordEnum;
import com.zgy.snr.common.enums.KeywordIndexEnum;
import com.zgy.snr.common.utils.ProtocolHelper;
import org.springframework.stereotype.Component;

/**
 *
 * 设备机芯验证方下发实现
 * @author zgy
 * @data 2021/3/30 10:59
 */

@Component
public class CoreCheckFormat implements ICheckFormat {
    @Override
    public Boolean checkFormat(String recv) {
        return true;
    }

    @Override
    public String substring(String recv) {
        return recv;
    }
}
