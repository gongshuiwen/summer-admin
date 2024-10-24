package io.summernova.admin.core.domain.field;

import io.summernova.admin.core.domain.annotations.StringField;
import lombok.Getter;

import java.lang.reflect.Field;

/**
 * @author gongshuiwen
 */
@Getter
public class StringFieldDefinition extends FieldDefinition {

    private final int minLength;
    private final int maxLength;
    private final String defaultValue;

    public StringFieldDefinition(Field field, StringField stringFieldAnnotation) {
        super(field, FieldType.STRING,
                stringFieldAnnotation.name(),
                stringFieldAnnotation.description(),
                stringFieldAnnotation.requiredMode(),
                stringFieldAnnotation.accessMode(),
                stringFieldAnnotation.deprecated(),
                stringFieldAnnotation.persistent(),
                stringFieldAnnotation.persistName(),
                stringFieldAnnotation.nullable(),
                stringFieldAnnotation.defaultNull()
        );
        this.minLength = stringFieldAnnotation.minLength();
        this.maxLength = stringFieldAnnotation.maxLength();
        this.defaultValue = stringFieldAnnotation.defaultValue();
    }
}
