package io.summernova.admin.core.domain.field.annotations;

import java.lang.annotation.*;

/**
 * @author gongshuiwen
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Many2ManyField {

    String sourceField();

    String targetField();

    String joinTable();
}
