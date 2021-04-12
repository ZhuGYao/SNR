package com.zgy.snr.common.check;

import com.zgy.snr.common.IDataComplete;
import com.zgy.snr.common.enums.CommonLengthEnum;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 获取公共完成方法
 * @author zgy
 * @data 2021/4/1 18:47
 */

@Component
public class CommonComplete {

    /**
     * 普通一问一答
     * @return
     */
    public IDataComplete getNormal() {
        return recv -> true;
    }

    /**
     * 四条应答全部返回验证
     * @return
     */
    public IDataComplete getAllData() {
        return new IDataComplete() {

            List<String> list = new ArrayList<>();
            @Override
            public Boolean isComplete(String recv) {
                list.add(recv);
                return list.size() == CommonLengthEnum.COMPLETE.value;
            }
        };
    }
}
