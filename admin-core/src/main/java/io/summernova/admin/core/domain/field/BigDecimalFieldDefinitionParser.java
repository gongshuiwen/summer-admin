package io.summernova.admin.core.domain.field;

import io.summernova.admin.core.domain.annotations.BigDecimalField;

import java.lang.reflect.Field;
import java.math.BigDecimal;

/**
 * @author gongshuiwen
 */
public class BigDecimalFieldDefinitionParser implements FieldDefinitionParser<BigDecimalFieldDefinition> {

    @Override
    public boolean check(Field field) {
        return field.getType() == BigDecimal.class && field.isAnnotationPresent(BigDecimalField.class);
    }

    @Override
    public BigDecimalFieldDefinition parse(Field field) {
        return new BigDecimalFieldDefinition(field, field.getDeclaredAnnotation(BigDecimalField.class));
    }
}
