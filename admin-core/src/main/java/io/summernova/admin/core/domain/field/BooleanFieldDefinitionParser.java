package io.summernova.admin.core.domain.field;

import io.summernova.admin.core.domain.annotations.BooleanField;

import java.lang.reflect.Field;

/**
 * @author gongshuiwen
 */
public class BooleanFieldDefinitionParser implements FieldDefinitionParser<BooleanFieldDefinition> {

    @Override
    public boolean check(Field field) {
        return field.getType() == Boolean.class && field.isAnnotationPresent(BooleanField.class);
    }

    @Override
    public BooleanFieldDefinition parse(Field field) {
        return new BooleanFieldDefinition(field, field.getDeclaredAnnotation(BooleanField.class));
    }
}
