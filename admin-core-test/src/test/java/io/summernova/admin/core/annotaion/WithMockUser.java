package io.summernova.admin.core.annotaion;

import java.lang.annotation.*;

/**
 * @author gongshuiwen
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface WithMockUser {

    long userId() default 0L;

    String[] roles() default {"USER"};

    String[] authorities() default {};

    String password() default "password";
}