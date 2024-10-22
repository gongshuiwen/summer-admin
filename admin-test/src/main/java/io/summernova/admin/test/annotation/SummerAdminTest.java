package io.summernova.admin.test.annotation;

import io.summernova.admin.test.context.BaseContextExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.*;

/**
 * @author gongshuiwen
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ExtendWith(BaseContextExtension.class)
public @interface SummerAdminTest {
}