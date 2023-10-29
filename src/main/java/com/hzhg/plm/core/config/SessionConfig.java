package com.hzhg.plm.core.config;

import org.springframework.boot.autoconfigure.session.RedisSessionProperties;
import org.springframework.boot.autoconfigure.session.SessionProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.session.config.annotation.web.http.EnableSpringHttpSession;
import org.springframework.session.data.redis.RedisSessionRepository;

import java.time.Duration;

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(RedisSessionProperties.class)
@EnableSpringHttpSession
public class SessionConfig {

    @Bean
    public RedisSessionRepository sessionRepository(
            RedisOperations<String, Object> redisOperations,
            SessionProperties sessionProperties,
            RedisSessionProperties redisSessionProperties) {

        RedisSessionRepository sessionRepository = new RedisSessionRepository(redisOperations);

        Duration timeout = sessionProperties.getTimeout();
        if (timeout != null) {
            sessionRepository.setDefaultMaxInactiveInterval(timeout);
        }

        sessionRepository.setRedisKeyNamespace(redisSessionProperties.getNamespace());
        sessionRepository.setFlushMode(redisSessionProperties.getFlushMode());
        sessionRepository.setSaveMode(redisSessionProperties.getSaveMode());
        return sessionRepository;
    }
}