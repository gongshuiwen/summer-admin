package io.summernova.admin.core.domain.field;

import io.summernova.admin.core.domain.annotations.IntegerField;

import java.lang.reflect.Field;

/**
 * @author gongshuiwen
 */
public class IntegerFieldDefinitionParser implements FieldDefinitionParser<IntegerFieldDefinition> {

    @Override
    public boolean check(Field field) {
        return field.getType() == Integer.class && field.isAnnotationPresent(IntegerField.class);
    }

    @Override
    public IntegerFieldDefinition parse(Field field) {
        return new IntegerFieldDefinition(field, field.getDeclaredAnnotation(IntegerField.class));
    }
}
