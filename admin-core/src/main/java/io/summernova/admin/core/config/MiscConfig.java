package io.summernova.admin.core.config;

import io.summernova.admin.core.mapper.MapperBeanPostProcessor;
import io.summernova.admin.core.util.SpringContextUtil;
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

    @Bean
    MapperBeanPostProcessor mapperBeanPostProcessor() {
        return new MapperBeanPostProcessor();
    }
}
