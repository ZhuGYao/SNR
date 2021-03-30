package com.zgy.snr.datasource;

import com.zgy.snr.common.enums.KeywordEnum;
import com.zgy.snr.common.utils.NettyByteAndStringUtils;
import com.zgy.snr.common.utils.ProtocolHelper;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 发送消息流程类
 * @author zgy
 * @data 2021/3/17 12:45
 */

@Slf4j
@Component
public class SendProcess {

    // 客户端对象，因为只有一个客户端，所以直接声明一个变量进行保存
    private ChannelHandlerContext context;

    public void setContext(ChannelHandlerContext context) {
        this.context = context;
    }

    /**
     * 机芯数据的组装发送
     * @param key
     * @param data
     */
    public void sendCoreData(String key, String data) {
        data = ProtocolHelper.addChannelHeadLengthTail(KeywordEnum.CHECK_OR_OCCUPANCY.value + key + data);
        sendData(key, data);
    }

    /**
     * 发送总口
     * @param data
     */
    private void sendData(String key, String data) {
        log.info("sendData:关键字:{}-|-命令:{}",key, data);
        context.writeAndFlush(NettyByteAndStringUtils.hexToByte(data));
    }
}
