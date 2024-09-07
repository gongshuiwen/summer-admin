package io.summernova.admin.core.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import io.summernova.admin.core.context.BaseContext;
import io.summernova.admin.core.context.BaseContextHolder;
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
                if (metaObject.hasSetter(CREATE_TIME_FIELD_NAME))
                    metaObject.setValue(CREATE_TIME_FIELD_NAME, now);
                if (metaObject.hasSetter(UPDATE_TIME_FIELD_NAME))
                    metaObject.setValue(UPDATE_TIME_FIELD_NAME, now);

                Long userId = getUserId();
                if (metaObject.hasSetter(CREATE_USER_FIELD_NAME))
                    metaObject.setValue(CREATE_USER_FIELD_NAME, userId);
                if (metaObject.hasSetter(UPDATE_USER_FIELD_NAME))
                    metaObject.setValue(UPDATE_USER_FIELD_NAME, userId);
            }

            @Override
            public void updateFill(MetaObject metaObject) {
                if (metaObject.hasSetter(UPDATE_TIME_FIELD_NAME))
                    metaObject.setValue(UPDATE_TIME_FIELD_NAME, LocalDateTime.now());

                if (metaObject.hasSetter(UPDATE_USER_FIELD_NAME))
                    metaObject.setValue(UPDATE_USER_FIELD_NAME, getUserId());
            }

            private long getUserId() {
                BaseContext baseContext = BaseContextHolder.getContext();
                return baseContext != null ? baseContext.getUserId() : 0L;
            }
        };
    }
}
