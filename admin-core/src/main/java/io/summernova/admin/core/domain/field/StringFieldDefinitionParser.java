package io.summernova.admin.core.domain.field;

import io.summernova.admin.core.domain.annotations.StringField;

import java.lang.reflect.Field;

/**
 * @author gongshuiwen
 */
public class StringFieldDefinitionParser implements FieldDefinitionParser<StringFieldDefinition> {

    @Override
    public boolean check(Field field) {
        return field.getType() == String.class && field.isAnnotationPresent(StringField.class);
    }

    @Override
    public StringFieldDefinition parse(Field field) {
        if (!check(field)) return null;
        return new StringFieldDefinition(field, field.getDeclaredAnnotation(StringField.class));
    }
}
