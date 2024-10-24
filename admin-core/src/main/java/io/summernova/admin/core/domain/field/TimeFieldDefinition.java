package io.summernova.admin.core.domain.field;

import io.summernova.admin.core.domain.annotations.TimeField;
import lombok.Getter;

import java.lang.reflect.Field;

/**
 * @author gongshuiwen
 */
@Getter
public class TimeFieldDefinition extends FieldDefinition {

    public TimeFieldDefinition(Field field, TimeField timeFieldAnnotation) {
        super(field, FieldType.TIME,
                timeFieldAnnotation.name(),
                timeFieldAnnotation.description(),
                timeFieldAnnotation.requiredMode(),
                timeFieldAnnotation.accessMode(),
                timeFieldAnnotation.deprecated(),
                timeFieldAnnotation.persistent(),
                timeFieldAnnotation.persistName(),
                timeFieldAnnotation.nullable(),
                timeFieldAnnotation.defaultNull()
        );
    }
}
