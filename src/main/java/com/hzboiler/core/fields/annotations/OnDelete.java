package com.hzboiler.core.fields.annotations;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface OnDelete {

    Type value();

    enum Type {
        RESTRICT,
        CASCADE,
        SET_NULL
    }
}
