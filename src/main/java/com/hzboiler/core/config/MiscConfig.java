package com.hzboiler.core.config;

import com.hzboiler.core.util.SpringContextUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MiscConfig {

    @Bean
    SpringContextUtil springContextUtil() {
        return new SpringContextUtil();
    }
}
