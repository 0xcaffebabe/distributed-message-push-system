package wang.ismy.push.connector;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * @author MY
 * @date 2020/6/16 20:12
 */
@Slf4j
@Component
@AllArgsConstructor
@ChannelHandler.Sharable
public class ConnectorHandler extends ChannelInboundHandlerAdapter {
    private final ClientService clientService;
    private final MessageService messageService;
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        clientService.clientActive(ctx.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        clientService.clientInActive(ctx.channel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.info("{}发生异常{}",ctx.channel(),cause);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        byte[] bytes = new byte[buf.readableBytes()];
        buf.readBytes(bytes);
        messageService.read(ctx.channel(),bytes);
        log.info("接收到数据:{}", new String(bytes));
        buf.release();
    }
}
