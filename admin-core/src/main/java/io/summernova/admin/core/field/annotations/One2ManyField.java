package io.summernova.admin.core.field.annotations;

import java.lang.annotation.*;

/**
 * @author gongshuiwen
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface One2ManyField {

    String inverseField();
}
