package org.rain.server;

/**
 * @Author: wcy
 * @Date: 2021/7/21
 */
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.springframework.beans.factory.annotation.Value;
import org.rain.common.coder.Decoder;
import org.rain.common.coder.Encoder;
import org.rain.server.handler.InitStartHandler;
import org.rain.server.handler.ServerHandler;

import javax.annotation.PreDestroy;

public class Server {

    @Value("${server.port:8070")
    private Integer inetPort;

    private static NioEventLoopGroup bossGroup = new NioEventLoopGroup();
    private static NioEventLoopGroup workerGroup = new NioEventLoopGroup();

    public static void main(String[] args) {
        InitStartHandler.initialize();

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap
                .group(bossGroup, workerGroup)

                // 指定Channel
                .channel(NioServerSocketChannel.class)

                //服务端可连接队列数,对应TCP/IP协议listen函数中backlog参数
                .option(ChannelOption.SO_BACKLOG, 1024)

                //设置TCP长连接,一般如果两个小时内没有数据的通信时,TCP会自动发送一个活动探测数据报文
                .childOption(ChannelOption.SO_KEEPALIVE, true)

                //将小的数据包包装成更大的帧进行传送，提高网络的负载
                .childOption(ChannelOption.TCP_NODELAY, true)

                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) {
                        ch.pipeline().addLast(new Decoder());
                        ch.pipeline().addLast(new Encoder());
                        ch.pipeline().addLast(new ServerHandler());
                    }
                });

        serverBootstrap.bind(8070);
    }

    @PreDestroy
    public void destory() throws InterruptedException {
        bossGroup.shutdownGracefully().sync();
        workerGroup.shutdownGracefully().sync();
    }

}