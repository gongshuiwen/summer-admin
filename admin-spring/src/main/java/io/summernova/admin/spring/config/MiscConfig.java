package io.summernova.admin.spring.config;

import io.summernova.admin.spring.util.SpringContextUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author gongshuiwen
 */
@Configuration
public class MiscConfig {

    @Bean
    SpringContextUtil springContextUtil() {
        return new SpringContextUtil();
    }
}
