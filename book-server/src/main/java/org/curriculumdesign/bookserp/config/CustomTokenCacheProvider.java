package org.curriculumdesign.bookserp.config;

import org.cdteam.spring.cloud.starter.security.token.TokenCacheProvider;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.stereotype.Component;

@Component
public class CustomTokenCacheProvider implements TokenCacheProvider {
    CacheManager cacheManager = new ConcurrentMapCacheManager();

    String cacheName = "TOKEN";

    @Override
    public void tokenCreate(String key, String value, long tokenValidityInSeconds) {
        Cache cache = cacheManager.getCache(cacheName);
        cache.put(key, value);
    }

    @Override
    public Object getValue(String key) {
        Cache cache = cacheManager.getCache(cacheName);
        return cache.get(key, String.class);
    }

    @Override
    public Long getExpire(String key) {
        return 100L;
    }

    @Override
    public void expire(String key, long expireTime) {
        Cache cache = cacheManager.getCache(cacheName);
        String valueWrapper = cache.get(key, String.class);
        tokenCreate(key, valueWrapper, expireTime);
    }
}
