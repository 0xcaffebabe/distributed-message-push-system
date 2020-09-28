package wang.ismy.push.admin.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import wang.ismy.push.common.MockUtils;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
public class RedisServiceTest {

    @Test
    public void getAllClient() {
        StringRedisTemplate redisTemplate = mock(StringRedisTemplate.class);
        int limit = 0;
        int length = 100;
        Set<String> mockSet = new HashSet<>();
        for (int i = 0; i < length; i++) {
            mockSet.add(UUID.randomUUID().toString());
        }
        when(redisTemplate.keys(eq("connector-*-*"))).thenReturn(mockSet);

        RedisService redisService = new RedisService(redisTemplate);
        var set = redisService.getAllClient(limit,length);

        assertEquals(mockSet,set);
    }

    @Test
    public void getTTLWhenExpireNull(){
        StringRedisTemplate redisTemplate = mock(StringRedisTemplate.class);
        String key = "test-key";
        when(redisTemplate.getExpire(eq(key),eq(TimeUnit.SECONDS))).thenReturn(null);
        RedisService redisService = new RedisService(redisTemplate);

        assertEquals(-1, redisService.getTTL(key));
    }

    @Test
    public void getTTLWhenExpireNotNull(){
        StringRedisTemplate redisTemplate = mock(StringRedisTemplate.class);
        String key = "test-key";
        when(redisTemplate.getExpire(eq(key),eq(TimeUnit.SECONDS))).thenReturn(100L);
        RedisService redisService = new RedisService(redisTemplate);

        assertEquals(100, redisService.getTTL(key));
    }
}