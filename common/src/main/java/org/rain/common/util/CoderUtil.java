package org.rain.common.util;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.nio.charset.Charset;

/**
 * @Author: wcy
 * @Date: 2021/7/21
 */
public class CoderUtil {

    /**
     * 数据解析
     */
    public static ByteBuf getByteBuf(ChannelHandlerContext ctx, String str) {
        // 1. 获取二进制抽象 ByteBuf
        ByteBuf buffer = ctx.alloc().buffer();

        // 2. 准备数据，指定字符串的字符集为 utf-8
        byte[] bytes = str.getBytes(Charset.forName("utf-8"));

        // 3. 填充数据到 ByteBuf
        buffer.writeBytes(bytes);

        return buffer;
    }

}
