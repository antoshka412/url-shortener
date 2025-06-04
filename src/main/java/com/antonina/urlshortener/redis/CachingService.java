package com.antonina.urlshortener.redis;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CachingService {

    private final StringRedisTemplate redisTemplate;

    public CachingService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public Optional<String> getOriginalUrl(String shortCode) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(shortCode));
    }

    public void cacheOriginalUrl(String shortCode, String originalUrl) {
        redisTemplate.opsForValue().set(shortCode, originalUrl);
    }
}

