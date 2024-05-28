package com.hzboiler.erp.core.field.annotations;

import java.lang.annotation.*;

/**
 * @author gongshuiwen
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface InverseField {

    String value();
}
