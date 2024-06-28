package com.cookshare.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisService {
    private final RedisTemplate<String, String> redisTemplate;

    @Autowired
    public RedisService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void setKeyValueWithExpire(String key, String value, long timeout, TimeUnit unit) {
        try {
            ValueOperations<String, String> values = redisTemplate.opsForValue();
            values.set(key, value, timeout, unit);
        } catch (Exception e) {
            System.err.println("Error saving data in Redis: " + e.getMessage());
        }
    }


    public void saveToken(String username, String refreshToken, long duration) {
        String key = "REFRESH_TOKEN:" + username;
        System.out.println("Saving token to Redis with key: " + key + " and duration: " + duration + " seconds.");
        redisTemplate.opsForValue().set(key, refreshToken, duration, TimeUnit.MILLISECONDS);
        System.out.println("Token saved successfully.");
    }

    public String getRefreshToken(String username) {
        String key = "REFRESH_TOKEN:" + username;
        try {
            return redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            System.err.println("Error retrieving data from Redis: " + e.getMessage());
            return null;
        }
    }

    public void deleteToken(String mobileNumber) {
        String key = "REFRESH_TOKEN:" + mobileNumber;
        System.out.println(key);
        try {
            boolean result = redisTemplate.delete(key);
            if (result) {
                System.out.println("Redis 삭제 성공 : " + key);
            } else {
                System.out.println("Redis 삭제 성공 : " + key);
            }
        } catch (Exception e) {
            System.err.println("Error deleting token from Redis: " + e.getMessage());
        }
    }

    public void blacklistToken(String token, long duration) {
        String key = "BLACKLISTED_TOKEN:" + token;
        try {
            redisTemplate.opsForValue().set(key, "blacklisted", duration, TimeUnit.SECONDS);
            System.out.println("블랙리스트 토큰 추가: " + key + ", 기간: " + duration + "초");
        } catch (Exception e) {
            System.err.println("토큰 블랙리스트 추가 실패: " + e.getMessage());
        }
    }


    public boolean isTokenBlacklisted(String key) {
        return redisTemplate.hasKey(key); // 블랙리스트 토큰이 있는지 확인
    }


}
