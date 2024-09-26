package io.summernova.admin.core.field.annotations;

import java.lang.annotation.*;

/**
 * @author gongshuiwen
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Many2OneField {

    OnDeleteType onDelete() default OnDeleteType.RESTRICT;
}