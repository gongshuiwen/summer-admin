package io.summernova.admin.core.domain.field;

import io.summernova.admin.core.domain.annotations.DateField;
import lombok.Getter;

import java.lang.reflect.Field;

/**
 * @author gongshuiwen
 */
@Getter
public class DateFieldDefinition extends FieldDefinition {

    public DateFieldDefinition(Field field, DateField dateFieldAnnotation) {
        super(field, FieldType.DATE,
                dateFieldAnnotation.name(),
                dateFieldAnnotation.description(),
                dateFieldAnnotation.requiredMode(),
                dateFieldAnnotation.accessMode(),
                dateFieldAnnotation.deprecated(),
                dateFieldAnnotation.persistent(),
                dateFieldAnnotation.persistName(),
                dateFieldAnnotation.nullable(),
                dateFieldAnnotation.defaultNull()
        );
    }
}
