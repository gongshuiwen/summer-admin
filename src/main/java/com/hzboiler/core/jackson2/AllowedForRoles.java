package com.hzboiler.core.jackson2;

import java.lang.annotation.*;

/**
 * @author gongshuiwen
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AllowedForRoles {
    String[] value();
}