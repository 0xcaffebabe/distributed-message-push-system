package wang.ismy.push.connector;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wang.ismy.push.common.entity.Message;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author MY
 * @date 2020/6/16 20:58
 */
@Service
@Slf4j
public class ClientService {

    /**
     * 存储userId与channel映射
     */
    private Map<String,Channel> channelMap = new ConcurrentHashMap<>(256);
    /**
     * 存储channel与userId映射
     */
    private Map<Channel,String> channelReverseMap = new ConcurrentHashMap<>(256);

    private final RedisService redisService;

    @Autowired
    private ThreadPoolService threadPoolService;

    public ClientService(RedisService redisService) {
        this.redisService = redisService;
    }

    public void clientActive(Channel channel){
        log.info("{}连接",channel);
    }

    /**
     * 客户断开连接回调
     * @param channel
     */
    public void clientInActive(Channel channel){
        if (!channelReverseMap.containsKey(channel)){
            return;
        }
        String userId = channelReverseMap.get(channel);
        channelReverseMap.remove(channel);
        channelMap.remove(userId);
        log.info("{}断开连接",channel);
        log.info(channelMap.toString());
        log.info(channelReverseMap.toString());
    }

    /**
     * 客户心跳包发送回调
     * @param channel
     * @param userId
     */
    public void flushClientLiveTime(Channel channel,String userId){

        channelMap.put(userId,channel);
        channelReverseMap.put(channel,userId);

        redisService.set(userId,"k");

        // 发送一条消息给客户端
        sendMessage(userId, LocalDateTime.now().toString());

        log.info("{},{}刷新存活时间",channel,userId);
        log.info(channelMap.toString());
        log.info(channelReverseMap.toString());

    }

    public Channel getClient(String userId){
        return channelMap.get(userId);
    }

    public String getClient (Channel channel){
        return channelReverseMap.get(channel);
    }

    public void sendMessage(String userId,String message){
        message = message+"\n";
        Channel channel = channelMap.get(userId);
        if (channel == null){
            log.info("获取channel失败:{}",userId);
            return;
        }
        if (!channel.isActive()) {
            log.info("{}非active",channel);
            return;
        }
        channel.write(Unpooled.wrappedBuffer(message.getBytes()));
        channel.flush();
    }

    public void broadcast(String message){
        Collection<Channel> channels = channelMap.values();
        for (Channel channel : channels) {
            String finalMessage = message+"\n";
            threadPoolService.submit(()->{
                channel.write(Unpooled.wrappedBuffer(finalMessage.getBytes()));
                channel.flush();
                log.info("向{}广播消息:{}",channel,message);
            });
        }
    }


}
