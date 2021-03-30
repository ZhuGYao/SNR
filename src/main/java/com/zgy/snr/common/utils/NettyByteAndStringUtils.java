package com.zgy.snr.common.utils;

import io.netty.buffer.ByteBuf;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 字节处理
 * @author zgy
 * @data 2021/3/15 15:55
 */

public class NettyByteAndStringUtils {

    private final static ReentrantLock Lock = new ReentrantLock();

    /**
     * byte数组转hex
     */
    public static String byteToHex(byte[] bytes) {
        final ReentrantLock putLock = Lock;
        putLock.lock();
        try {
            String strHex = "";
            StringBuilder sb = new StringBuilder("");
            for (byte aByte : bytes) {
                strHex = Integer.toHexString(aByte & 0xFF);
                // 每个字节由两个字符表示，位数不够，高位补0
                sb.append((strHex.length() == 1) ? "0" + strHex : strHex);
            }
            return sb.toString().toUpperCase().trim();
        } finally {
            putLock.unlock();
        }
    }

    /**
     * hex转byte数组
     */
    public static byte[] hexToByte(String hex) {
        final ReentrantLock putLock = Lock;
        putLock.lock();
        try {
            int m = 0, n = 0;
            // 每两个字符描述一个字节
            int byteLen = hex.length() / 2;
            byte[] ret = new byte[byteLen];
            for (int i = 0; i < byteLen; i++) {
                m = i * 2 + 1;
                n = m + 1;
                int intVal = Integer.decode("0x" + hex.substring(i * 2, m) + hex.substring(m, n));
                ret[i] = (byte) intVal;
            }
            return ret;
        } finally {
            putLock.unlock();
        }
    }

    /**
     * 字节数组的大小
     */
    public static int getByteSize(byte[] bytes) {
        final ReentrantLock putLock = Lock;
        putLock.lock();
        try {
            int size = 0;
            if (bytes == null || bytes.length == 0) {
                return 0;
            }
            for (byte aByte : bytes) {
                if (aByte == '\0') {
                    break;
                }
                size++;
            }
            return size;
        } finally {
            putLock.unlock();
        }
    }


    public static String convertByteBufToString(ByteBuf buf) {
        String str;
        if (buf.hasArray()) { // 处理堆缓冲区
            str = new String(buf.array(), buf.arrayOffset() + buf.readerIndex(), buf.readableBytes());
        } else { // 处理直接缓冲区以及复合缓冲区
            byte[] bytes = new byte[buf.readableBytes()];
            buf.getBytes(buf.readerIndex(), bytes);
            str = new String(bytes, 0, buf.readableBytes());
        }
        return str;
    }

}
