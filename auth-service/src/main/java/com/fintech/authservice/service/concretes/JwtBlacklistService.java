package com.fintech.authservice.service.concretes;

import java.time.Duration;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class JwtBlacklistService {

    private final StringRedisTemplate redis;
    
    public JwtBlacklistService(StringRedisTemplate redis) {
        this.redis = redis; 
    }

    private String key(String jti) {
        return "blacklist:jwt:" + jti;
    }

    public void blacklist(String jti, long ttlSeconds) {
        if (ttlSeconds <= 0) return;
        redis.opsForValue().set(key(jti), "1", Duration.ofSeconds(ttlSeconds));
    }

    public boolean isBlacklisted(String jti) {
        return Boolean.TRUE.equals(redis.hasKey(key(jti)));
    }
}