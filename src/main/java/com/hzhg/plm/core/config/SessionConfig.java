package com.hzhg.plm.core.config;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.security.jackson2.SecurityJackson2Modules;

import static com.hzhg.plm.core.jackson2.RoleBasedAnnotationFilter.ROLE_BASED_FILTER_ID;

/**
 * Refer to <a href="https://github.com/spring-projects/spring-session/blob/main/spring-session-samples/spring-session-sample-boot-redis-json/src/main/java/sample/config/SessionConfig.java">Spring Session Github</a>
 */
@Configuration
public class SessionConfig implements BeanClassLoaderAware {

    private ClassLoader loader;

    /**
     * Note that the bean name for this bean is intentionally
     * {@code springSessionDefaultRedisSerializer}. It must be named this way to override
     * the default {@link RedisSerializer} used by Spring Session.
     */
    @Bean
    public RedisSerializer<Object> springSessionDefaultRedisSerializer() {
        return new GenericJackson2JsonRedisSerializer(objectMapper());
    }

    /**
     * Customized {@link ObjectMapper} for Spring Session
     * @return the {@link ObjectMapper} to use
     */
    private ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();

        // Set Visibility
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

        // Activate default typing
        objectMapper.activateDefaultTyping(
                LaissezFaireSubTypeValidator.instance,
                ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.PROPERTY);

        // Register modules from SecurityJackson2Modules
        // Note: already include com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
        objectMapper.registerModules(SecurityJackson2Modules.getModules(loader));

        // Add dummy ROLE_BASED_FILTER
        objectMapper.setFilterProvider(new SimpleFilterProvider()
                .addFilter(ROLE_BASED_FILTER_ID, new SimpleBeanPropertyFilter(){}));

        return objectMapper;
    }

    /**
     * @see
     * org.springframework.beans.factory.BeanClassLoaderAware#setBeanClassLoader(java.lang.ClassLoader)
     */
    @Override
    public void setBeanClassLoader(@NotNull ClassLoader classLoader) {
        loader = classLoader;
    }
}