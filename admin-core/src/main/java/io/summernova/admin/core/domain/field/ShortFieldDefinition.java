package io.summernova.admin.core.domain.field;

import io.summernova.admin.core.domain.annotations.ShortField;
import lombok.Getter;

import java.lang.reflect.Field;

/**
 * @author gongshuiwen
 */
@Getter
public class ShortFieldDefinition extends FieldDefinition {

    private final short minValue;
    private final short maxValue;
    private final short defaultValue;

    public ShortFieldDefinition(Field field, ShortField shortFieldAnnotation) {
        super(field, FieldType.SHORT,
                shortFieldAnnotation.name(),
                shortFieldAnnotation.description(),
                shortFieldAnnotation.requiredMode(),
                shortFieldAnnotation.accessMode(),
                shortFieldAnnotation.deprecated(),
                shortFieldAnnotation.persistent(),
                shortFieldAnnotation.persistName(),
                shortFieldAnnotation.nullable(),
                shortFieldAnnotation.defaultNull()
        );
        this.minValue = shortFieldAnnotation.minValue();
        this.maxValue = shortFieldAnnotation.maxValue();
        this.defaultValue = shortFieldAnnotation.defaultValue();
    }
}
