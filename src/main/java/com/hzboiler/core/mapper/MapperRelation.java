package com.hzboiler.core.mapper;

import com.hzboiler.core.model.BaseModel;

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

    Class<? extends BaseModel> class1();

    String field2();

    Class<? extends BaseModel> class2();
}
