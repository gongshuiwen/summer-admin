package com.hzboiler.core.mapper;

import com.hzboiler.core.entity.BaseEntity;

import java.lang.annotation.*;

/**
 * @author gongshuiwen
 */
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
