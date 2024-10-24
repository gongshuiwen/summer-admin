package io.summernova.admin.core.domain.field;

import io.summernova.admin.core.domain.annotations.LongField;
import lombok.Getter;

import java.lang.reflect.Field;

/**
 * @author gongshuiwen
 */
@Getter
public class LongFieldDefinition extends FieldDefinition {

    private final long minValue;
    private final long maxValue;
    private final long defaultValue;

    public LongFieldDefinition(Field field, LongField longFieldAnnotation) {
        super(field, FieldType.LONG,
                longFieldAnnotation.name(),
                longFieldAnnotation.description(),
                longFieldAnnotation.requiredMode(),
                longFieldAnnotation.accessMode(),
                longFieldAnnotation.deprecated(),
                longFieldAnnotation.persistent(),
                longFieldAnnotation.persistName(),
                longFieldAnnotation.nullable(),
                longFieldAnnotation.defaultNull()
        );
        this.minValue = longFieldAnnotation.minValue();
        this.maxValue = longFieldAnnotation.maxValue();
        this.defaultValue = longFieldAnnotation.defaultValue();
    }
}
