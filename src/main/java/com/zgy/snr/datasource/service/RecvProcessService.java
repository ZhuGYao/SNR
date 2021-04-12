package com.zgy.snr.datasource.service;

import com.zgy.snr.common.ICheckFormat;
import com.zgy.snr.common.IDataComplete;
import com.zgy.snr.common.IVerification;
import com.zgy.snr.common.check.BoardCheckFormat;
import com.zgy.snr.common.check.CommonComplete;
import com.zgy.snr.common.check.CoreCheckFormat;
import com.zgy.snr.datasource.RecvProcess;
import io.reactivex.Observable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 数据处理业务
 * @author zgy
 * @data 2021/3/17 10:32
 */

@Slf4j
@Service
public class RecvProcessService {

    @Autowired
    private RecvProcess recvProcess;

    @Autowired
    private CoreCheckFormat coreCheckFormat;

    @Autowired
    private BoardCheckFormat boardCheckFormat;

    @Autowired
    private CommonComplete commonComplete;

    /**
     * 机芯验证,无需重复接收
     * @param verification
     * @return
     */
    public Observable<String> processCore(IVerification verification){
        return processCore(verification, commonComplete.getNormal());
    }

    /**
     * 机芯验证下发方法,需重复接收
     * @param verification
     * @return
     */
    public Observable<String> processCore(IVerification verification, IDataComplete dataComplete){
        return process(verification, dataComplete, coreCheckFormat);
    }


    /**
     * 接口板验证,无需重复接收
     * @param verification
     * @return
     */
    public Observable<String> processBoard(IVerification verification){
        return processBoard(verification, commonComplete.getNormal());
    }

    /**
     * 接口板验证下发方法,需重复接收
     * @param verification
     * @return
     */
    public Observable<String> processBoard(IVerification verification, IDataComplete dataComplete){
        return process(verification, dataComplete, boardCheckFormat);
    }


    /**
     * 需重复接收,自定义实现完成接口
     * @param verification
     * @param dataComplete
     * @return
     */
    public Observable<String> process(IVerification verification, IDataComplete dataComplete, ICheckFormat checkFormat) {
        return Observable.create(observableEmitter -> recvProcess.doRecvPackage(recv -> {
            if (checkFormat.checkFormat(recv)) {
                // 将数据进行截取
                String recv_data = checkFormat.substring(recv);
                log.debug("core_recv:{}",recv_data);
                // 回调验证并下发
                if (verification.isSuccess(recv_data)) {
                    observableEmitter.onNext(recv_data);
                    // 是否完成接收
                    if (dataComplete.isComplete(recv_data)) {
                        observableEmitter.onComplete();
                        return true;
                    }
                }
            }
            return false;
        }));
    }
}
