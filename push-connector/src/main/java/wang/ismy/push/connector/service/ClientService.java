package wang.ismy.push.connector.service;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wang.ismy.push.common.AESUtils;

import java.time.LocalDateTime;
import java.util.Base64;
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

    public Collection<String> getClients(){
        return channelReverseMap.values();
    }

    public void sendMessage(String userId,String message){
        String key = redisService.get("encrypt-"+userId);
        if (StringUtils.isEmpty(key)){
            log.warn("客户 {} 无法获取到密钥", userId);
            return;
        }
        message = Base64.getEncoder().encodeToString(AESUtils.encrypt(message.getBytes(), key));
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

    public void broadcast(String msg){
        Collection<Channel> channels = channelMap.values();
        for (Channel channel : channels) {
            threadPoolService.submit(()->{
                String userId = getClient(channel);
                if (StringUtils.isEmpty(userId)){
                    log.warn("连接 {} 不在系统之内", channel);
                    return;
                }
                String key = redisService.get("encrypt-"+userId);
                if (StringUtils.isEmpty(key)){
                    log.warn("客户 {} 无法获取到密钥", userId);
                    return;
                }
                String message = Base64.getEncoder().encodeToString(AESUtils.encrypt(msg.getBytes(), key));
                message = message+"\n";
                channel.write(Unpooled.wrappedBuffer(message.getBytes()));
                channel.flush();
                log.info("向{}广播消息:{}",channel,message);
            });
        }
    }


}
