package com.hzboiler.erp.spring.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.hzboiler.erp.core.context.BaseContextHolder;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

/**
 * @author gongshuiwen
 */
@Configuration
public class MyBatisPlusConfig {

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }

    @Bean
    public MetaObjectHandler metaObjectHandler() {
        return new MetaObjectHandler() {

            private static final String CREATE_TIME_FIELD_NAME = "createTime";
            private static final String UPDATE_TIME_FIELD_NAME = "updateTime";
            private static final String CREATE_USER_FIELD_NAME = "createUser";
            private static final String UPDATE_USER_FIELD_NAME = "updateUser";

            @Override
            public void insertFill(MetaObject metaObject) {
                LocalDateTime now = LocalDateTime.now();
                Long userId = BaseContextHolder.getContext().getUserId();
                metaObject.setValue(CREATE_TIME_FIELD_NAME, now);
                metaObject.setValue(UPDATE_TIME_FIELD_NAME, now);
                metaObject.setValue(CREATE_USER_FIELD_NAME, userId);
                metaObject.setValue(UPDATE_USER_FIELD_NAME, userId);
            }

            @Override
            public void updateFill(MetaObject metaObject) {
                metaObject.setValue(UPDATE_TIME_FIELD_NAME, LocalDateTime.now());
                metaObject.setValue(UPDATE_USER_FIELD_NAME, BaseContextHolder.getContext().getUserId());
            }
        };
    }
}
