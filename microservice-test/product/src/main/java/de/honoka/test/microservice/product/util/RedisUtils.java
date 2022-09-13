package de.honoka.test.microservice.product.util;

import com.alibaba.fastjson.JSON;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtils {

    @Value("${spring.application.name}")
    private String applicationName;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private RedissonClient redissonClient;

    private String getRealKey(String key) {
        return applicationName + ":" + key;
    }

    private String getStringValue(Object value) {
        String stringValue;
        if(value instanceof String) {
            stringValue = (String) value;
        } else if(value instanceof Number) {
            stringValue = value.toString();
        } else {
            stringValue = JSON.toJSONString(value);
        }
        return stringValue;
    }

    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(getRealKey(key), getStringValue(value));
    }

    public void set(String key, Object value, int expire, TimeUnit timeUnit) {
        set(key, value);
        expire(key, expire, timeUnit);
    }

    public String get(String key) {
        Object value = redisTemplate.opsForValue().get(getRealKey(key));
        if(value == null) return null;
        return value.toString();
    }

    public <T> T get(String key, Class<T> clazz) {
        return JSON.parseObject(get(key), clazz);
    }

    public void expire(String key, int expire, TimeUnit timeUnit) {
        redisTemplate.expire(getRealKey(key), expire, timeUnit);
    }

    public void remove(String key) {
        redisTemplate.delete(getRealKey(key));
    }

    public boolean hasKey(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(getRealKey(key)));
    }

    public RLock getLock(String key) {
        return redissonClient.getLock(getRealKey(key));
    }
}