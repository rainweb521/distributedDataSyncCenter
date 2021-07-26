package org.rain.server.handler;

/**
 * @Author: wcy
 * @Date: 2021/7/21
 */
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.rain.common.domain.vo.MessageBaseVO;
import org.rain.server.data.DataMessageQueue;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static cn.hutool.core.lang.Console.log;

public class ServerHandler extends ChannelInboundHandlerAdapter {

    public static ChannelGroup users = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    public Map<String,Channel> channelMap = new HashMap<>();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        MessageBaseVO message = (MessageBaseVO) msg;
        Channel mychannel = ctx.channel();
        log(new Date() + ": 服务端读到数据 -> " + message);
        DataMessageQueue.add(message);
        if (message.getMsgType() == 0){
            channelMap.put(message.getClientId(),mychannel);
        }
        if (message.getMsgType()==1){
            channelMap.forEach((key,value)-> users.writeAndFlush(message, channel -> channel != mychannel));
        }else if (message.getMsgType()==2&&channelMap.containsKey(message.getGoalClient())){
            channelMap.get(message.getGoalClient()).writeAndFlush(message);
        }

    }
    /**
     * 当客户连接服务端之后（打开链接） 获取客户端的channel，并且放到ChannelGroup中去进行管理
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        users.add(ctx.channel());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {

        String channelId = ctx.channel().id().asLongText();
        System.out.println("客户端被移除，channelId为：" + channelId);

        // 当触发handlerRemoved，ChannelGroup会自动移除对应的客户端channel
        users.remove(ctx.channel());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception { // (5)
        Channel incoming = ctx.channel();
        System.out.println("SimpleChatClient:"+incoming.remoteAddress()+"在线");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception { // (6)
        Channel incoming = ctx.channel();
        System.out.println("SimpleChatClient:"+incoming.remoteAddress()+"掉线");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        // 发生异常之后关键channel。随后从ChannelGroup 中移除
        ctx.channel().close();
        users.remove(ctx.channel());
    }




}
