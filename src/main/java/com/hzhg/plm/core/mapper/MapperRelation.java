package com.hzhg.plm.core.mapper;

import com.hzhg.plm.core.entity.BaseEntity;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface MapperRelation {

    String table();

    String field1();

    Class<? extends BaseEntity> class1();

    String field2();

    Class<? extends BaseEntity> class2();
}
