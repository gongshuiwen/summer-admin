package io.summernova.admin.core.domain.field;

import io.summernova.admin.core.domain.annotations.DateField;

import java.lang.reflect.Field;
import java.time.LocalDate;

/**
 * @author gongshuiwen
 */
public class DateFieldDefinitionParser implements FieldDefinitionParser<DateFieldDefinition> {

    @Override
    public boolean check(Field field) {
        return field.getType() == LocalDate.class && field.isAnnotationPresent(DateField.class);
    }

    @Override
    public DateFieldDefinition parse(Field field) {
        return new DateFieldDefinition(field, field.getDeclaredAnnotation(DateField.class));
    }
}
