package io.summernova.admin.core.domain.field;

import io.summernova.admin.core.domain.annotations.IntegerField;
import lombok.Getter;

import java.lang.reflect.Field;

/**
 * @author gongshuiwen
 */
@Getter
public class IntegerFieldDefinition extends FieldDefinition {

    private final int minValue;
    private final int maxValue;
    private final int defaultValue;

    public IntegerFieldDefinition(Field field, IntegerField integerFieldAnnotation) {
        super(field, FieldType.INTEGER,
                integerFieldAnnotation.name(),
                integerFieldAnnotation.description(),
                integerFieldAnnotation.requiredMode(),
                integerFieldAnnotation.accessMode(),
                integerFieldAnnotation.deprecated(),
                integerFieldAnnotation.persistent(),
                integerFieldAnnotation.persistName(),
                integerFieldAnnotation.nullable(),
                integerFieldAnnotation.defaultNull()
        );
        this.minValue = integerFieldAnnotation.minValue();
        this.maxValue = integerFieldAnnotation.maxValue();
        this.defaultValue = integerFieldAnnotation.defaultValue();
    }
}
