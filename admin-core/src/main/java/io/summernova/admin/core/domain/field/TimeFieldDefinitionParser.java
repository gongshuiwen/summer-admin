package io.summernova.admin.core.domain.field;

import io.summernova.admin.core.domain.annotations.TimeField;

import java.lang.reflect.Field;
import java.time.LocalTime;

/**
 * @author gongshuiwen
 */
public class TimeFieldDefinitionParser implements FieldDefinitionParser<TimeFieldDefinition> {

    @Override
    public boolean check(Field field) {
        return field.getType() == LocalTime.class && field.isAnnotationPresent(TimeField.class);
    }

    @Override
    public TimeFieldDefinition parse(Field field) {
        return new TimeFieldDefinition(field, field.getDeclaredAnnotation(TimeField.class));
    }
}
