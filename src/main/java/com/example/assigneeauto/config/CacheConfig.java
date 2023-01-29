package com.example.assigneeauto.config;

import com.example.assigneeauto.config.key.generator.PercentWeightKeyGenerator;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.context.WebApplicationContext;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    @Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
    public CacheManager cacheManager() {

        return new ConcurrentMapCacheManager();
    }

    @Bean("percentWeightKeyGenerator")
    public KeyGenerator keyGenerator() {
        return new PercentWeightKeyGenerator();
    }
}
