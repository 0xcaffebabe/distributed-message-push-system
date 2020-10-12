package wang.ismy.push.connector.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import wang.ismy.push.connector.service.ClientService;

import java.util.Base64;
import java.util.List;

/**
 * @author MY
 * @date 2020/10/12 20:00
 */
public class Base64Handler extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        byte[] bytes = new byte[in.readableBytes()];
        in.readBytes(bytes);
        ByteBuf byteBuf = Unpooled.wrappedBuffer(Base64.getDecoder().decode(bytes));
        out.add(byteBuf);
    }
}
