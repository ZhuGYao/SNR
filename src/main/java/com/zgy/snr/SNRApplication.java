package com.zgy.snr;

import com.zgy.snr.netty.NettyServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableAsync
@MapperScan(basePackages = "com.zgy.snr.mapper")
public class SNRApplication {

    public static void main(String[] args)  throws Exception {
        SpringApplication.run(SNRApplication.class, args);
        //启动TCP服务端,加入spring容器统一管理
        NettyServer nettyServer = SpringUtils.getBean(NettyServer.class);
        nettyServer.run();
    }
}
