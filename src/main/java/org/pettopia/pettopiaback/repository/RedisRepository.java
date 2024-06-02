package org.pettopia.pettopiaback.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class RedisRepository {

    private final RedisTemplate<String, Object> redisTemplate;

    @Value("${jwt.refresh-exp-time}")
    public long expirationTimeMillis;


    @Autowired
    public RedisRepository(RedisTemplate<String, Object> redisTemplate) {
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        this.redisTemplate = redisTemplate;
    }

    public void saveRefreshToken(String userId, String refreshToken) {
//        redisTemplate.opsForValue().set(userId, refreshToken);
        redisTemplate.opsForValue().set(userId, refreshToken, expirationTimeMillis, TimeUnit.MILLISECONDS);
        log.info("Refresh Token saved for userId: " + userId + " with expiration: " + expirationTimeMillis + " ms");
    }

    public void deleteRefreshToken(String userId){
        redisTemplate.delete(userId);
    }

    public String getRefreshToken(String userId) {
        String refreshToken =  (String) redisTemplate.opsForValue().get(userId);

        if (refreshToken == null) {
            log.warn("No Refresh Token found for userId: " + userId);
        }
        return refreshToken;
    }


}