package io.summernova.admin.core.annotaion;

import java.lang.annotation.*;

/**
 * @author gongshuiwen
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WithMockAdmin {
}