package wang.ismy.push.connector;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author MY
 * @date 2020/6/17 19:49
 */
@Service
public class RedisService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Value("${connector.port}")
    private int port;

    public void set(String key,String value){
        redisTemplate.opsForValue().set("connector-"+port+"-"+key,value,30L, TimeUnit.SECONDS);
    }
}
