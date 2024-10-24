package io.summernova.admin.core.domain.field;

import io.summernova.admin.core.domain.annotations.ShortField;

import java.lang.reflect.Field;

/**
 * @author gongshuiwen
 */
public class ShortFieldDefinitionParser implements FieldDefinitionParser<ShortFieldDefinition> {

    @Override
    public boolean check(Field field) {
        return field.getType() == Short.class && field.isAnnotationPresent(ShortField.class);
    }

    @Override
    public ShortFieldDefinition parse(Field field) {
        return new ShortFieldDefinition(field, field.getDeclaredAnnotation(ShortField.class));
    }
}
