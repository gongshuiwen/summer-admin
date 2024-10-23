package io.summernova.admin.core.domain.annotations;

import io.summernova.admin.core.domain.defaults.DefaultValueProvider;
import io.summernova.admin.core.domain.model.BaseModel;

import java.lang.annotation.*;

/**
 * @author gongshuiwen
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BooleanField {

    /**
     * the name of the model's field, used for serialization and deserialization
     * if not specified, the name of the java field is used.
     */
    String name() default "";

    String description() default "";

    RequiredMode requiredMode() default RequiredMode.AUTO;

    AccessMode accessMode() default AccessMode.AUTO;

    boolean deprecated() default false;

    boolean persistent() default true;

    String persistName() default "";

    boolean nullable() default true;

    boolean defaultNull() default false;

    boolean defaultValue() default false;
}
