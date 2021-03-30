package com.zgy.snr.common.check;

import com.zgy.snr.common.IVerification;
import com.zgy.snr.common.enums.KeywordIndexEnum;
import com.zgy.snr.common.utils.ProtocolHelper;
import org.springframework.stereotype.Component;

/**
 * 获取校验接口实现
 * @author zgy
 * @data 2021/3/18 14:11
 */

@Component
public class CommonVerification {

    /**
     * 获取机芯的验证回调
     * @param key 关键字
     * @return 是否符合
     */
    public IVerification getIsCoreVerification(String key) {

        return recv_data -> {
            if (recv_data.length() < KeywordIndexEnum.CORE_DATA_START_INDEX_RESPONSE.value + key.length()) return false;
            return ProtocolHelper.subString(KeywordIndexEnum.CORE_DATA_START_INDEX_RESPONSE.value, key.length(), recv_data).equals(key);
        };
    }

}
