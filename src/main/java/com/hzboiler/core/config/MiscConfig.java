package com.hzboiler.core.config;

import com.hzboiler.core.utils.SpringContextUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MiscConfig {

    @Bean
    SpringContextUtils springContextUtils() {
        return new SpringContextUtils();
    }
}
