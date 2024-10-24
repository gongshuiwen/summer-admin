package io.summernova.admin.core.domain.field;

import io.summernova.admin.core.domain.annotations.DateTimeField;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

/**
 * @author gongshuiwen
 */
public class DateTimeFieldDefinitionParser implements FieldDefinitionParser<DateTimeFieldDefinition> {

    @Override
    public boolean check(Field field) {
        return field.getType() == LocalDateTime.class && field.isAnnotationPresent(DateTimeField.class);
    }

    @Override
    public DateTimeFieldDefinition parse(Field field) {
        return new DateTimeFieldDefinition(field, field.getDeclaredAnnotation(DateTimeField.class));
    }
}
