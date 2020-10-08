package wang.ismy.push.lookup.service;

import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

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
}
