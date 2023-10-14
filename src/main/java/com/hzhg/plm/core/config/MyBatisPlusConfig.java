package com.hzhg.plm.core.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.hzhg.plm.core.utils.BaseContext;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

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
                metaObject.setValue(CREATE_TIME_FIELD_NAME, LocalDateTime.now());
                metaObject.setValue(UPDATE_TIME_FIELD_NAME, LocalDateTime.now());
                metaObject.setValue(CREATE_USER_FIELD_NAME, BaseContext.getCurrentUserId());
                metaObject.setValue(UPDATE_USER_FIELD_NAME, BaseContext.getCurrentUserId());
            }

            @Override
            public void updateFill(MetaObject metaObject) {
                metaObject.setValue(UPDATE_TIME_FIELD_NAME, LocalDateTime.now());
                metaObject.setValue(UPDATE_USER_FIELD_NAME, BaseContext.getCurrentUserId());
            }
        };
    }
}
