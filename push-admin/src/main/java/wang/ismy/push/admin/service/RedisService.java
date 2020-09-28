package wang.ismy.push.admin.service;

import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author MY
 * @date 2020/9/28 20:24
 */
@Service
@AllArgsConstructor
public class RedisService {
    private final StringRedisTemplate redisTemplate;

    public Set<String> getAllClient(int limit, int length){
        return redisTemplate.keys("connector-*-*").stream()
                .skip(limit)
                .limit(length)
                .collect(Collectors.toSet());
    }

    public int getTTL(String key){
        Long expire = redisTemplate.getExpire(key, TimeUnit.SECONDS);
        if (expire == null) {
            return -1;
        }
        return expire.intValue();
    }
}
