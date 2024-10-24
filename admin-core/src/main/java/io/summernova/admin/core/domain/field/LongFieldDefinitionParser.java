package io.summernova.admin.core.domain.field;

import io.summernova.admin.core.domain.annotations.LongField;

import java.lang.reflect.Field;

/**
 * @author gongshuiwen
 */
public class LongFieldDefinitionParser implements FieldDefinitionParser<LongFieldDefinition> {

    @Override
    public boolean check(Field field) {
        return field.getType() == Long.class && field.isAnnotationPresent(LongField.class);
    }

    @Override
    public LongFieldDefinition parse(Field field) {
        return new LongFieldDefinition(field, field.getDeclaredAnnotation(LongField.class));
    }
}
