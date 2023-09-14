package com.hzhg.plm.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    private static final String CREATE_TIME_FIELD_NAME = "createTime";
    private static final String UPDATE_TIME_FIELD_NAME = "updateTime";
    private static final String CREATE_BY_FIELD_NAME = "createUser";
    private static final String UPDATE_BY_FIELD_NAME = "updateUser";

    @Override
    public void insertFill(MetaObject metaObject) {
        metaObject.setValue(CREATE_TIME_FIELD_NAME, LocalDateTime.now());
        metaObject.setValue(UPDATE_TIME_FIELD_NAME, LocalDateTime.now());
        metaObject.setValue(CREATE_BY_FIELD_NAME, BaseContext.getCurrentUserId());
        metaObject.setValue(UPDATE_BY_FIELD_NAME, BaseContext.getCurrentUserId());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        metaObject.setValue(UPDATE_TIME_FIELD_NAME, LocalDateTime.now());
        metaObject.setValue(UPDATE_BY_FIELD_NAME, BaseContext.getCurrentUserId());
    }
}
