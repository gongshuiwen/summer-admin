package com.hzhg.plm.core.config;

import com.hzhg.plm.core.utils.SpringContextUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MiscConfig {

    @Bean
    SpringContextUtils springContextUtils() {
        return new SpringContextUtils();
    }
}
