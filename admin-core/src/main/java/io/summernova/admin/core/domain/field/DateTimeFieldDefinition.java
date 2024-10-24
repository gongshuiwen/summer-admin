package io.summernova.admin.core.domain.field;

import io.summernova.admin.core.domain.annotations.DateTimeField;
import lombok.Getter;

import java.lang.reflect.Field;

/**
 * @author gongshuiwen
 */
@Getter
public class DateTimeFieldDefinition extends FieldDefinition {

    public DateTimeFieldDefinition(Field field, DateTimeField dateTimeFieldAnnotation) {
        super(field, FieldType.DATETIME,
                dateTimeFieldAnnotation.name(),
                dateTimeFieldAnnotation.description(),
                dateTimeFieldAnnotation.requiredMode(),
                dateTimeFieldAnnotation.accessMode(),
                dateTimeFieldAnnotation.deprecated(),
                dateTimeFieldAnnotation.persistent(),
                dateTimeFieldAnnotation.persistName(),
                dateTimeFieldAnnotation.nullable(),
                dateTimeFieldAnnotation.defaultNull()
        );
    }
}
