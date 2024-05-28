package com.hzboiler.erp.core.config;

import com.hzboiler.erp.core.util.SpringContextUtil;
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
