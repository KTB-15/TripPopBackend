package com.kakaotech.back.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;

@Configuration
public class RedisConfig {

    @Bean
    public JedisPool jedisPoolConnection() {
        return new JedisPool("127.0.0.1", 6379);
    }
}
