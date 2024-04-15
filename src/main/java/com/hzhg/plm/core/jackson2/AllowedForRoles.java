package com.hzhg.plm.core.jackson2;

import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AllowedForRoles {
    String[] value();
}