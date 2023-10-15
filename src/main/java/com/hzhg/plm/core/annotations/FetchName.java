package com.hzhg.plm.core.annotations;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hzhg.plm.core.entity.BaseEntity;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
public @interface FetchName {

    String idField();
    Class<? extends BaseMapper<? extends BaseEntity>> mapper();
}
