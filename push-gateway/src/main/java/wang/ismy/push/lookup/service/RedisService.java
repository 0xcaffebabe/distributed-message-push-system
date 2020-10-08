package wang.ismy.push.lookup.service;

import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author MY
 * @date 2020/10/5 21:02
 */
@Service
@AllArgsConstructor
public class RedisService {
    private final StringRedisTemplate redisTemplate;

    public void hashIncr(String key,String field){
        redisTemplate.opsForHash().increment(key, field, 1);
    }

    public void setToken(String userId, String token){
        redisTemplate.setEnableTransactionSupport(true);
        redisTemplate.multi();
        redisTemplate.opsForValue().set("gateway-token-" + userId, token,30, TimeUnit.MINUTES);
        redisTemplate.opsForValue().set("gateway-userid-" + token, userId,30, TimeUnit.MINUTES);
        redisTemplate.exec();
    }

    public String getUserIdBy(String token){
        return redisTemplate.opsForValue().get(token);
    }

    public void setEncryptKey(String userId, String encryptKey){
        redisTemplate.opsForValue().set("encrypt-" + userId, encryptKey,30, TimeUnit.MINUTES);
    }
}
