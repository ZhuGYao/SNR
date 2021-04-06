package com.zgy.snr.common.utils;

import java.math.BigInteger;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import com.zgy.snr.common.enums.KeywordEnum;
import com.zgy.snr.common.enums.TimeoutEnum;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProtocolHelper {

    private static final String zero = "0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";
    private static final String f = "FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF";

    public static String hexString(int value, int len) {
        String stmp = Integer.toHexString(value).toUpperCase();
        if (stmp.length() >= len) {
            return stmp;
        }
        return zero.substring(0,len - stmp.length()) + stmp;
    }

    /**
     * 补位0
     * @param value
     * @param len
     * @return
     */
    public static String addZero(String value, int len){
        if (value.length() >= len) {
            return value;
        }
        return value + zero.substring(0,len-value.length());
    }

    /**
     * 补位F
     * @param value
     * @param len
     * @return
     */
    public static String addFF(String value, int len){
        if (value.length() >= len) {
            return value;
        }
        return value + f.substring(0,len-value.length());
    }


    /**
     * 同步执行命令，返回执行结果（T）
     * @param observable 监听对象
     * @param timeout 超时时间 -1则不设置超时
     * @param defaultValue 默认类型
     * @param <T> 返回值
     * @return data
     */
    public static <T>  T commonBlockingSubscribe(Observable<T> observable, Long timeout, T defaultValue) {
        ObservableResult<T> result = new ObservableResult<>();
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Disposable subscribe = observable.subscribe(value -> {
            log.debug("commonBlockingSubscribe:" + value);
            result.setValue(value);
            countDownLatch.countDown();
        });

        // 等待超时
        await(countDownLatch, timeout);

        if (!subscribe.isDisposed()) {
            subscribe.dispose();
        }
        return result.getValue();
    }

    /**
     * 等待睡眠
     * @param timeout 等待时间
     */
    public static void await(Long timeout) {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        await(countDownLatch, timeout);
    }

    /**
     * 睡眠
     * @param timeout
     */
    public static void sleep(Long timeout) {
        try {
            Thread.sleep(timeout);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 等待
     * @param countDownLatch 锁
     * @param timeout 等待时间
     */
    public static void await(CountDownLatch countDownLatch,Long timeout) {
        try {
            // 是否需要超时返回
            if (timeout == TimeoutEnum.NOT_TIMEOUT.value) {
                countDownLatch.await();
            } else {
                // 超时返回
                countDownLatch.await(timeout, TimeUnit.MILLISECONDS);
            }
        }catch (InterruptedException e) {
            log.error("countDownLatch:" + e.getMessage());
        }
    }

    /**
     *  截取字符串
     * @param startIndex 开始位置
     * @param num 截取几位
     * @param data 数据
     * @return
     */
    public static String subString(Integer startIndex, Integer num, String data) {
        return data.substring(startIndex, startIndex + num);
    }

    /**
     * 机芯命令封装
     * @param data
     * @return
     */
    public static String addCoreHeadLengthTail(String data) {
        byte len = (byte)(data.length() / 2);
        return KeywordEnum.CORE_HEAD.value + ProtocolHelper.hexString(len,2) + data + KeywordEnum.CORE_TAIL.value;
    }

    /**
     * 接口板命令封装
     * @param data
     * @return
     */
    public static String addChannelHeadLengthTail(String data) {
        byte len = (byte)(data.length() / 2 + 1);
        return KeywordEnum.CHANNEL_HEAD.value + ProtocolHelper.hexString(len,4) + data + KeywordEnum.CHECK_OR_OCCUPANCY.value + KeywordEnum.CHANNEL_TAIL.value;
    }


    public static String hexString(String str) {
        assert str != null && str.length() > 0;
        StringBuffer sb = new StringBuffer();
        for (char ch : str.toCharArray()){
            sb.append(hexString(ch,2));
        }
        return sb.toString();
    }
    public static int dec2hex(int dec) {
        int d = dec / 16;
        int m = dec % 16;
        return d * 10 + m;
    }
    public static int hex2dec(int hex) {
        int i = 1;
        int dec = 0;
        int mod ;
        while(hex > 0){
            mod = hex % (i * 10);
            dec += mod * xNy(16,i - 1);
            hex /= 10;
            ++i;
        }
        return dec;
    }
    public static int hex2dec(String hex) {
        BigInteger bi = new BigInteger(hex,16);
        return bi.intValue();
    }
    public static long xNy(long x,long y){
        long result = 1;
        for (int i = 0 ; i < y;++i){
            result *= x;
        }
        return result;
    }
}
