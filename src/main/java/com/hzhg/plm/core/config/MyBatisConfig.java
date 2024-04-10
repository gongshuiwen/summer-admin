package com.hzhg.plm.core.config;

import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.hzhg.plm.core.fields.Many2One;
import com.hzhg.plm.core.mybatis.Many2OneTypeHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyBatisConfig {

    @Bean
    public ConfigurationCustomizer mybatisConfigurationCustomizer() {
        return configuration -> configuration.getTypeHandlerRegistry().register(Many2One.class, new Many2OneTypeHandler());
    }
}