package wang.ismy.push.connector.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.MessageToMessageDecoder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import wang.ismy.push.common.AESUtils;
import wang.ismy.push.connector.service.ClientService;
import wang.ismy.push.connector.service.RedisService;

import java.util.List;

/**
 * @author MY
 * @date 2020/10/12 20:13
 */
@ChannelHandler.Sharable
@AllArgsConstructor
@Service
@Slf4j
public class DecryptHandler extends MessageToMessageDecoder<ByteBuf> {

    private final ClientService clientService;
    private final RedisService redisService;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        byte[] bytes = new byte[msg.readableBytes()];
        msg.readBytes(bytes);
        String user = clientService.getClient(ctx.channel());
        if (StringUtils.isEmpty(user)){
            log.warn("系统内找不到该客户 {} 的映射", ctx.channel());
            ctx.close();
            return;
        }
        String key = redisService.get("encrypt-" + user);
        if (StringUtils.isEmpty(key)) {
            log.warn("无法获取到客户 {} 的密钥", user);
            return;
        }
        ByteBuf byteBuf = Unpooled.wrappedBuffer(AESUtils.decrypt(bytes, key));
        out.add(byteBuf);
    }
}
