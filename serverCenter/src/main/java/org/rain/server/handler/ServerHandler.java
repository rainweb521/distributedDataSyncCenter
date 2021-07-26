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
import lombok.extern.slf4j.Slf4j;
import org.rain.common.domain.vo.MessageBaseVO;
import org.rain.server.data.ClientChannel;
import org.rain.server.data.DataMessageQueue;

import java.util.Date;


@Slf4j
public class ServerHandler extends ChannelInboundHandlerAdapter {

    private static ChannelGroup users = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private ClientChannel clientChannel = new ClientChannel();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        MessageBaseVO message = (MessageBaseVO) msg;
        Channel mychannel = ctx.channel();
        log.info(new Date() + ": 服务端读到数据 -> " + message);
        if (message.getMsgType() == 0){
            clientChannel.putChannel(message.getClientId(),mychannel);
            return;
        }
        DataMessageQueue.add(message);
        if (message.getMsgType()==1){
            clientChannel.getManagers().forEach((key,value)-> users.writeAndFlush(message, channel -> channel != mychannel));
        }else if (message.getMsgType()==2&&clientChannel.containsKey(message.getGoalClient())){
            clientChannel.getChannel(message.getGoalClient()).writeAndFlush(message);
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
        log.info("客户端被移除，channelId为：" + channelId);

        // 当触发handlerRemoved，ChannelGroup会自动移除对应的客户端channel
        users.remove(ctx.channel());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception { // (5)
        Channel incoming = ctx.channel();
        log.info("SimpleChatClient:"+incoming.remoteAddress()+"在线");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception { // (6)
        Channel incoming = ctx.channel();
        log.info("SimpleChatClient:"+incoming.remoteAddress()+"掉线");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        // 发生异常之后关键channel。随后从ChannelGroup 中移除
        log.error(cause.getMessage());
        ctx.channel().close();
        users.remove(ctx.channel());
    }




}
