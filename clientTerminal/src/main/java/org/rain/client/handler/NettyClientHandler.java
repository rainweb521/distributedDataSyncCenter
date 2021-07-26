package org.rain.client.handler;

import cn.hutool.core.util.IdUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
//import lombok.extern.slf4j.Slf4j;
import org.rain.common.domain.vo.MessageBaseVO;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import static cn.hutool.core.lang.Console.log;

/**
 * @Author: wcy
 * @Date: 2021/7/21
 */
//@Slf4j
public class NettyClientHandler extends ChannelInboundHandlerAdapter {


    private ChannelHandlerContext context;
    private AtomicInteger atomicInteger ;
    private String clientId;
    private String clientName;

    public NettyClientHandler(AtomicInteger atomicInteger, String clientId, String clientName) {
        this.atomicInteger = atomicInteger;
        this.clientId = clientId;
        this.clientName = clientName;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        this.context = ctx;

            MessageBaseVO message = MessageBaseVO.builder().id(atomicInteger.getAndIncrement())
                    .msgId(IdUtil.randomUUID())
                    .msgType(0)
                    .clientId(clientId)
                    .clientName(clientName)
                    .timestamp(System.currentTimeMillis())
                    .message("str").build();
            // 2. 写数据
            log("客户端写出数据:{}",message);
            ctx.channel().writeAndFlush(message);
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        log(new Date() + ": 客户端读到数据 -> " + msg.toString());
//        context.writeAndFlush(getByteBuf(ctx,"已收到"));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println(cause.getMessage());
    }
}