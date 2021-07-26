package org.rain.common.coder;

import cn.hutool.json.JSONUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.rain.common.domain.vo.MessageBaseVO;

import java.nio.charset.Charset;
import java.util.List;

/**
 * @Author: wcy
 * @Date: 2021/7/21
 */
public class Decoder extends ByteToMessageDecoder {
    private Class<?> aClass;

    public Decoder(Class<?> aClass) {
        this.aClass = aClass;
    }

    public Decoder() {
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf in, List<Object> list)  {
        // 可读字节不够长度
        if (in.readableBytes() < 4){
            return;
        }
        in.markReaderIndex();      // 设置读开始的标志位，当数据无法完成业务时，可复位
        int dataLength = in.readInt();
        if (in.readableBytes() < dataLength){
            in.resetReaderIndex();       // 复位
            return;
        }
        byte[] data = new byte[dataLength];
        in.readBytes(data);
        list.add(JSONUtil.toBean(new String(data,Charset.defaultCharset()), MessageBaseVO.class));
    }
}
