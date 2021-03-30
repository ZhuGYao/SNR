package com.zgy.snr.netty;

import com.zgy.snr.datasource.RecvDataSource;
import com.zgy.snr.datasource.SendProcess;
import com.zgy.snr.common.utils.NettyByteAndStringUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author zgy
 * @data 2021/3/14 10:22
 */

@Slf4j
@Component
@ChannelHandler.Sharable
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    private static Lock lock_1 = new ReentrantLock();

    private static Lock lock_2 = new ReentrantLock();

    private static Lock lock_3 = new ReentrantLock();

    private static Lock lock_4 = new ReentrantLock();

    @Autowired
    private RecvDataSource recvDataSource;

    @Autowired
    private SendProcess sendProcess;

    /**
     * 管理一个全局map，保存连接进服务端的通道数量
     */
    private static final ConcurrentHashMap<ChannelId, ChannelHandlerContext> CHANNEL_MAP = new ConcurrentHashMap<>();

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {

    }

    /**
     * 处理异常, 一般是需要关闭通道
     * @param ctx
     * @param cause
     *
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
        log.info("服务端异常关闭" + ctx.channel());
    }


    /**
     * @param ctx
     *
     * @DESCRIPTION: 有客户端连接服务器会触发此函数
     * @return: void
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        lock_1.lock();
        try {
            //获取连接通道唯一标识
            ChannelId channelId = ctx.channel().id();
            //如果map中不包含此连接，就保存连接
            if (CHANNEL_MAP.containsKey(channelId)) {
                log.info("客户端【" + channelId + "】是连接状态，连接通道数量: " + CHANNEL_MAP.size());
            } else {
                //保存连接
                CHANNEL_MAP.put(channelId, ctx);
                // 保存连接
                sendProcess.setContext(ctx);

                log.info("客户端【" + channelId + "】连接netty服务器");
                log.info("连接通道数量: " + CHANNEL_MAP.size());
            }
        } catch (Exception e) {
            log.error("客户端连接错误:" + e.getMessage());
        } finally {
            lock_1.unlock();
        }
    }

    /**
     * @param ctx
     *
     * @DESCRIPTION: 有客户端终止连接服务器会触发此函数
     * @return: void
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        lock_2.lock();
        try {
            ChannelId channelId = ctx.channel().id();
            //包含此客户端才去删除
            if (CHANNEL_MAP.containsKey(channelId)) {
                //删除连接
                CHANNEL_MAP.remove(channelId);
                System.out.println();
                log.info("客户端【" + channelId + "】退出netty服务器");
                log.info("连接通道数量: " + CHANNEL_MAP.size());
            }
        } catch (Exception e) {
            log.error("客户端断开连接错误:" + e.getMessage());
        } finally {
            lock_2.unlock();
        }
    }


    /**
     * 1. ChannelHandlerContext ctx:上下文对象, 含有 管道pipeline , 通道channel, 地址
     * 2. Object msg: 就是客户端发送的数据 默认Object
     * <p>
     * 读取数据实际(这里我们可以读取客户端发送的消息)
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        lock_3.lock();
        try {
//            log.info("服务器读取线程 " + Thread.currentThread().getName() + " channle = " + ctx.channel());
            Channel channel = ctx.channel();
            // 将 msg 转成一个 ByteBuf
            // ByteBuf 是 Netty 提供的，不是 NIO 的 ByteBuffer.
            ByteBuf buf = (ByteBuf) msg;
            //得到此时客户端的数据长度
            int bytes_length = buf.readableBytes();
            //组件新的字节数组
            byte[] buffer = new byte[bytes_length];
            buf.readBytes(buffer);
            final String allData = NettyByteAndStringUtils.byteToHex(buffer);

            // 数据下发
            recvDataSource.dataEntry(allData);

            // 回收ByteBuf,防止内存泄漏
            ReferenceCountUtil.release(buf);
            // 避免频繁接收
            Thread.sleep(100);

        } catch (Exception e) {
            log.error("读取数据失败:" + e.getMessage());
        } finally {
            lock_3.unlock();
        }
    }


    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
        lock_4.lock();
        try {
            String socketString = ctx.channel().remoteAddress().toString();
            if (evt instanceof IdleStateEvent) {
                IdleStateEvent event = (IdleStateEvent) evt;
                if (event.state() == IdleState.READER_IDLE) {
                    log.info("Client: " + socketString + " READER_IDLE 读超时");
                    ctx.disconnect();
                } else if (event.state() == IdleState.WRITER_IDLE) {
                    log.info("Client: " + socketString + " WRITER_IDLE 写超时");
                    ctx.disconnect();
                } else if (event.state() == IdleState.ALL_IDLE) {
                    log.info("Client: " + socketString + " ALL_IDLE 总超时");
                    ctx.disconnect();
                }
            }
        } catch (Exception e) {
            log.error("error:" + e.getMessage());
        } finally {
            lock_4.unlock();
        }
    }
}