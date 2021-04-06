package com.zgy.snr.udp;

import com.zgy.snr.common.utils.ProtocolHelper;
import com.zgy.snr.datasource.RecvDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * 简易UDP服务，如是UDP服务可借鉴使用
 * @author zgy
 * @data 2021/3/31 18:14
 */

@Slf4j
@Component
public class UDPServer {

    private DatagramSocket datagramSocket= null;
    @Value("${udp.ip}")
    private String ip;

    @Value("${udp.port}")
    private Integer port;

    @Autowired
    private RecvDataSource recvDataSource;

    /**
     * 初始化服务
     */
    public void run() {

        try {
            datagramSocket=new DatagramSocket();
            //接收数据
            while (datagramSocket != null) {
                DatagramPacket inputPacket = new DatagramPacket(new byte[1024], 1024);
                datagramSocket.receive(inputPacket);
                String data = new String(inputPacket.getData(), 0, inputPacket.getLength());
                log.info("recv data:{}", data);
                // 避免频繁接收
                ProtocolHelper.sleep(100L);
                recvDataSource.dataEntry(data);
            }

        } catch (Exception e) {
            log.debug("init server error" + e.getMessage());
        } finally {
            if (datagramSocket != null) {
                log.info("server close....");
                datagramSocket.close();
            }
        }
    }

    /**
     * 发送数据
     * @param message 消息
     * @return 发送是否成功
     */
    public Boolean send(String message) {
        try {
            InetAddress address= InetAddress.getByName(ip);
            log.info("send data:{}", message);
            byte[] buffer=message.getBytes("UTF-8");
            DatagramPacket packet=new DatagramPacket(buffer, buffer.length, address, port);
            datagramSocket.send(packet);
            return true;
        } catch (Exception e) {
            log.debug("send error:{}", e.getMessage());
            return false;
        }

    }
}
