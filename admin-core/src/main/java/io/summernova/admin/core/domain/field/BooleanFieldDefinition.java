package io.summernova.admin.core.domain.field;

import io.summernova.admin.core.domain.annotations.BooleanField;

import java.lang.reflect.Field;

/**
 * @author gongshuiwen
 */
public class BooleanFieldDefinition extends FieldDefinition {

    private final boolean defaultValue;

    public BooleanFieldDefinition(Field field, BooleanField booleanFieldAnnotation) {
        super(field, FieldType.BOOLEAN,
                booleanFieldAnnotation.name(),
                booleanFieldAnnotation.description(),
                booleanFieldAnnotation.requiredMode(),
                booleanFieldAnnotation.accessMode(),
                booleanFieldAnnotation.deprecated(),
                booleanFieldAnnotation.persistent(),
                booleanFieldAnnotation.persistName(),
                booleanFieldAnnotation.nullable(),
                booleanFieldAnnotation.defaultNull());
        this.defaultValue = booleanFieldAnnotation.defaultValue();
    }

    public boolean getDefaultValue() {
        return defaultValue;
    }
}
