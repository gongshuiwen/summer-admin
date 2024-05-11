package com.hzboiler.core.fields.annotations;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OnDelete {

    Type value();

    enum Type {
        RESTRICT,
        CASCADE,
        SET_NULL
    }
}
