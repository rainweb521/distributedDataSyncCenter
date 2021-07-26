package org.rain.common.coder;

import cn.hutool.json.JSONUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.nio.charset.Charset;

/**
 * @Author: wcy
 * @Date: 2021/7/21
 */
public class Encoder extends MessageToByteEncoder {
    private Class<?> aClass;

    public Encoder(Class<?> aClass) {
        this.aClass = aClass;
    }

    public Encoder() {
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) throws Exception {
        byte[] bytes = JSONUtil.toJsonStr(o).getBytes(Charset.defaultCharset());
        byteBuf.writeInt(bytes.length);
        byteBuf.writeBytes(bytes);
    }
}
