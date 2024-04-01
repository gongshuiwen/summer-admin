package com.hzhg.plm.core.mapper;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface MapperRelation {

    String table();

    String field1();

    String field2();
}
