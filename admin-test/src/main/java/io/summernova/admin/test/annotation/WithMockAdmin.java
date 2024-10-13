package io.summernova.admin.test.annotation;

import java.lang.annotation.*;

/**
 * @author gongshuiwen
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WithMockAdmin {
}