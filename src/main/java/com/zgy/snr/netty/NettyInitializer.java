package com.zgy.snr.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author zgy
 * @data 2021/3/14 10:22
 */
@Slf4j
@Component
@ChannelHandler.Sharable
public class NettyInitializer extends ChannelInitializer {

    @Autowired
    public MyByteEncoder myByteEncoder;
    @Autowired
    private NettyServerHandler nettyServerHandler;

    @Override
    protected void initChannel(Channel channel) {
        //创建一个通道初始化对象(匿名对象)
        //给pipeline 设置处理器
        //可以使用一个集合管理 SocketChannel,再推送消息时,可以将业务加入到各个channel对应的NIOEventLoop的taskQueue
        // 或者 scheduleTaskQueue
        //入站解码
        //channel.pipeline().addLast(new MyByteToLongDecoder2());
        //出站编码
        channel.pipeline().addLast(myByteEncoder);
        channel.pipeline().addLast(nettyServerHandler);
    }
}