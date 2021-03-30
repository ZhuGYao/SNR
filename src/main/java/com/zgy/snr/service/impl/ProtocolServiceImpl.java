package com.zgy.snr.service.impl;

import com.zgy.snr.common.check.CommonVerification;
import com.zgy.snr.common.utils.CommonResultUtils;
import com.zgy.snr.common.utils.ProtocolHelper;
import com.zgy.snr.datasource.SendProcess;
import com.zgy.snr.datasource.service.RecvProcessService;
import com.zgy.snr.service.ProtocolService;
import io.reactivex.Observable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zgy
 * @data 2021/3/30 16:43
 */

@Slf4j
@Service
public class ProtocolServiceImpl implements ProtocolService {

    @Autowired
    private RecvProcessService recvProcessService;

    @Autowired
    private SendProcess sendProcess;

    @Autowired
    private CommonVerification commonVerification;


    @Override
    public CommonResultUtils readTime() {
        return null;
    }

    @Override
    public CommonResultUtils readVersion() {
        return null;
    }

    /**
     * 发送消息
     * @param key 关键字
     * @param type 类型
     */
    private void send(String key, String type) {
        String sendData = ProtocolHelper.addCoreHeadLengthTail(key);
        sendProcess.sendCoreData(type, sendData);
    }

    /**
     * 监听接收消息
     * @param key 关键字
     * @param timeout 超时时间
     * @return
     */
    private String recv(String key, Long timeout) {
        Observable<String> observable = recvProcessService.processCore(commonVerification.getIsCoreVerification(key));
        return ProtocolHelper.commonBlockingSubscribe(observable, timeout,"");
    }
}
