package io.summernova.admin.core.mapper;

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

    Class<?> class1();

    String field2();

    Class<?> class2();
}
