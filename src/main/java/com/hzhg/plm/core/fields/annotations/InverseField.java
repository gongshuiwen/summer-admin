package com.hzhg.plm.core.fields.annotations;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface InverseField {

    String value();
}
