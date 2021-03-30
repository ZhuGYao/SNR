package com.zgy.snr.datasource;

import com.zgy.snr.common.AbstractCheckFormat;
import com.zgy.snr.common.IReplyCallback;
import com.zgy.snr.common.enums.CommonLengthEnum;
import com.zgy.snr.common.enums.KeywordEnum;
import com.zgy.snr.common.enums.KeywordIndexEnum;
import com.zgy.snr.common.enums.TimeoutEnum;
import com.zgy.snr.common.utils.ProtocolHelper;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 接收消息进行验证下发
 * @author zgy
 * @data 2021/3/17 10:15
 */

@Slf4j
@Component
public class RecvProcess extends AbstractCheckFormat implements InitializingBean {

    // 心跳标志
    private volatile Map<String, Boolean> heartbeatFlag = new HashMap<>();

    private Disposable core_a_timeout = null;
    private Disposable core_b_timeout = null;
    private Disposable core_c_timeout = null;

    @Autowired
    private RecvDataSource recvDataSource;

    private PublishSubject<String> packageRecv = PublishSubject.create();

    @Override
    public void afterPropertiesSet() {
        recvDataSource.getDataSourceRecv().subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(@NonNull Disposable disposable) {

            }

            @Override
            public void onNext(@NonNull String s) {

                // 如果不是心跳继续往下下发
                if (!checkFormat(s)) {
                    packageRecv.onNext(s);
                }
            }

            @Override
            public void onError(@NonNull Throwable throwable) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    /**
     * 消息监听验证回调
     * @param callback
     */
    public void doRecvPackage(IReplyCallback callback) {
        packageRecv.subscribe(new Observer<String>() {

            private Disposable disposable = null;
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                disposable = d;
            }

            @Override
            public void onNext(@NonNull String s) {
                if (callback.recvData(s)) {
                    log.info("doRecvPackage:" + s);
                    disposable.dispose();
                }
            }

            @Override
            public void onError(@NonNull Throwable throwable) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    /**
     * 监测心跳 TODO:超时需要直接发送websocket到前端
     * @param data
     * @return
     */
    @Override
    protected boolean checkFormat(String data) {

        return isAHeartbeat(data) ||
                isBHeartbeat(data) ||
                isCHeartbeat(data);
    }

    /**
     * 是否是A心跳
     * @param data
     * @return
     */
    private boolean isAHeartbeat(String data) {
        if (data.length() > CommonLengthEnum.A_HEART_LENGTH.value &&
                ProtocolHelper.subString(KeywordIndexEnum.CORE_DATA_START_INDEX.value + CommonLengthEnum.CORE_A_HEAD_LENGTH.value,
                        KeywordEnum.CORE_HEARTBEAT.value.length(), data).
                equals(KeywordEnum.CORE_HEARTBEAT.value)) {

            log.info("A heartbeat: {}", data);

            dispose(core_a_timeout);
            core_a_timeout = creatDisposable(TimeoutEnum.A_HEARTBEAT_TIMEOUT.value, KeywordEnum.CORE_A.value);
            heartbeatFlag.put(KeywordEnum.CORE_A.value, true);

            return true;
        }
        return false;
    }

    /**
     * 是否是B心跳
     * @param data
     * @return
     */
    private boolean isBHeartbeat(String data) {
        if (data.length() > CommonLengthEnum.B_HEART_LENGTH.value &&
                ProtocolHelper.subString(KeywordIndexEnum.CORE_DATA_START_INDEX.value + CommonLengthEnum.CORE_B_HEAD_LENGTH.value,
                        KeywordEnum.CORE_HEARTBEAT.value.length(), data).
                equals(KeywordEnum.CORE_HEARTBEAT.value)) {

            log.info("B heartbeat: {}", data);

            dispose(core_b_timeout);
            core_b_timeout = creatDisposable(TimeoutEnum.B_HEARTBEAT_TIMEOUT.value, KeywordEnum.CORE_B.value);
            heartbeatFlag.put(KeywordEnum.CORE_B.value, true);

            return true;
        }
        return false;
    }

    /**
     * 是否是C心跳
     * @param data
     * @return
     */
    private boolean isCHeartbeat(String data) {
        if (data.length() > CommonLengthEnum.C_HEART_LENGTH.value &&
                ProtocolHelper.subString(KeywordIndexEnum.CORE_DATA_START_INDEX.value + CommonLengthEnum.CORE_C_HEAD_LENGTH.value,
                        KeywordEnum.CORE_HEARTBEAT.value.length(), data).
                equals(KeywordEnum.CORE_HEARTBEAT.value)) {
            log.info("C heartbeat: {}", data);

            dispose(core_c_timeout);
            core_c_timeout = creatDisposable(TimeoutEnum.C_HEARTBEAT_TIMEOUT.value, KeywordEnum.CORE_C.value);
            heartbeatFlag.put(KeywordEnum.CORE_C.value, true);

            return true;
        }
        return false;
    }

    /**
     * 销毁公共方法
     * @param d
     */
    private void dispose(Disposable d) {
        if (d != null && !d.isDisposed()) {
            d.dispose();
            d = null;
        }
    }

    /**
     * 创建超时方法
     * @param delay
     * @param type
     * @return
     */
    private Disposable creatDisposable(Long delay, String type) {
        return Observable.timer(delay, TimeUnit.MILLISECONDS)
                .subscribe(aLong -> {
                    log.error("CORE：关键字{},心跳超时", type);
                    heartbeatFlag.put(type, false);
                });
    }
}
