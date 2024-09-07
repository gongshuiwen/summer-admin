package io.summernova.admin.core.jackson2;

import java.lang.annotation.*;

/**
 * @author gongshuiwen
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AllowedForCreateUser {
}