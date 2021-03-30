package com.zgy.snr.datasource;

import com.zgy.snr.common.AbstractCheckFormat;
import com.zgy.snr.common.enums.KeywordEnum;
import io.reactivex.subjects.PublishSubject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 数据总口
 * @author zgy
 * @data 2021/3/16 20:01
 */

@Slf4j
@Component
public class RecvDataSource extends AbstractCheckFormat {

    private PublishSubject<String> dataSourceRecv = PublishSubject.create();

    private StringBuffer resultBuffer = new StringBuffer(1024* 4);

    /**
     * 数据入口
     * @param msg
     */
    public void dataEntry(String msg) {

        // 验证是否符合规范
        if (checkFormat(msg)) {
            // 追加数据
            resultBuffer.append(msg);
            // 截取完整数据并下发
            completeData();
        }
    }

    public PublishSubject<String> getDataSourceRecv() {
        return dataSourceRecv;
    }

    @Override
    protected boolean checkFormat(String msg) {
        return true;
    }

    /**
     * 循环验证截取完整数据
     */
    private void completeData() {

        while (resultBuffer.length() > 0) {
            //判断是否存在HEAD
            int start = resultBuffer.indexOf(KeywordEnum.CHANNEL_HEAD.value);
            if (start < 0) {
                break;
            }
            // 判断是否存在TAIL
            int end = resultBuffer.indexOf(KeywordEnum.CHANNEL_TAIL.value, start);
            if (end < 0) {
                break;
            }

            // 如果都符合则进行截取
            String packageData = resultBuffer.substring(start, end + KeywordEnum.CHANNEL_TAIL.value.length());
            // 删除已经截取的数据
            resultBuffer.delete(start, end + KeywordEnum.CHANNEL_TAIL.value.length());

            // 往下传递
            dataSourceRecv.onNext(packageData);
        }
    }
}
