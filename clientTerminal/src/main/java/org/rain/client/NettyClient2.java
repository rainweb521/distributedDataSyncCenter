package org.rain.client;

/**
 * @Author: wcy
 * @Date: 2021/7/21
 */

import cn.hutool.core.util.IdUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.rain.client.handler.NettyClientHandler;
import org.rain.common.coder.Decoder;
import org.rain.common.coder.Encoder;
import org.rain.common.domain.vo.MessageBaseVO;

import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static cn.hutool.core.lang.Console.log;

public class NettyClient2 {

    private static String host = "127.0.0.1";
    private static int MAX_RETRY = 5;
    private static AtomicInteger atomicInteger = new AtomicInteger(0);
    private static final String clientId = IdUtil.objectId();
    private static final String clientName = "111";

    public static void main(String[] args) {
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();
        bootstrap
            // 1.指定线程模型
            .group(workerGroup)
            // 2.指定 IO 类型为 NIO
            .channel(NioSocketChannel.class)
            .option(ChannelOption.SO_KEEPALIVE, true)
            .option(ChannelOption.TCP_NODELAY, true)
            // 3.IO 处理逻辑
            .handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) {
                    ch.pipeline()
                        .addLast(new IdleStateHandler(0, 10, 0))
                        .addLast(new Encoder())
                        .addLast(new Decoder())
                        .addLast(new NettyClientHandler(atomicInteger,clientId,clientName));
                }
            });
        // 4.建立连接
        Channel channel = bootstrap.connect(host, 8070).addListener(future -> {
            if (future.isSuccess()) {
                System.out.println("连接成功!");
            } else {
                System.err.println("连接失败!");
            }

        }).channel();
        Scanner in = new Scanner(System.in);

        while(true){
            MessageBaseVO message = MessageBaseVO.builder().id(atomicInteger.getAndIncrement())
                    .msgId(IdUtil.simpleUUID())
                    .clientId(clientId)
                    .clientName(clientName)
                    .timestamp(System.currentTimeMillis())
                    .message(in.nextLine()).build();
            log("客户端写出数据:{}",message);
            channel.writeAndFlush( message);
        }
    }

    /**
     * 用于失败重连
     */
    private static Channel connect(Bootstrap bootstrap, String host, int port, int retry) {
        return bootstrap.connect(host, port).addListener(future -> {
            if (future.isSuccess()) {
                System.out.println("连接成功!");
            } else if (retry == 0) {
                System.err.println("重试次数已用完，放弃连接！");
            } else {
                // 第几次重连
                int order = (MAX_RETRY - retry) + 1;
                // 本次重连的间隔
                int delay = 1 << order;
                System.err.println(new Date() + ": 连接失败，第" + order + "次重连……");
                bootstrap.config().group().schedule(() -> connect(bootstrap, host, port, retry - 1), delay, TimeUnit
                        .SECONDS);
            }
        }).channel();
    }

}