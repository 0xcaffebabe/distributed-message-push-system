package wang.ismy.push.connector.handler;

import com.google.common.cache.CacheBuilder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import wang.ismy.push.common.AESUtils;
import wang.ismy.push.connector.service.ClientService;
import wang.ismy.push.connector.service.RedisService;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;

/**
 * @author MY
 * @date 2020/10/12 21:00
 */
@Component
@ChannelHandler.Sharable
@Slf4j
@AllArgsConstructor
public class AuthHandler extends ChannelInboundHandlerAdapter {

    private ClientService clientService;
    private RedisService redisService;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (clientService.getClient(ctx.channel()) != null){ ctx.fireChannelRead(msg);return; }

        ByteBuf byteBuf = (ByteBuf) msg;
        String authMsg = byteBuf.toString(StandardCharsets.UTF_8);
        if (authMsg.startsWith("auth-")){
            String[] split = authMsg.split("-");
            String userId = split[1];
            String encryptStr = split[2];

            String key = redisService.get("encrypt-" + userId);
            if (StringUtils.isEmpty(key)){
                log.warn("客户 {} 认证失败：key为null", ctx.channel());
                ctx.close();
                return;
            }
            String token = redisService.get("gateway-token-"+userId);
            if (StringUtils.isEmpty(token)){
                log.warn("客户 {} 认证失败：token为null", ctx.channel());
                ctx.close();
                return;
            }
            byte[] bytes = Base64.getDecoder().decode(encryptStr);
            byte[] decrypt = AESUtils.decrypt(bytes, key);
            if (new String(decrypt).equals(token)){
                clientService.flushClientLiveTime(ctx.channel(), userId);
                ctx.channel().writeAndFlush(Unpooled.wrappedBuffer("auth-success\r\n".getBytes()));
                log.info("用户 {} 认证成功", userId);
                return;
            }
        }
        ctx.writeAndFlush("kickout");
        ctx.close();
    }
}
