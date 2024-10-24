package io.summernova.admin.core.domain.field;

import io.summernova.admin.core.domain.annotations.BigDecimalField;
import lombok.Getter;

import java.lang.reflect.Field;
import java.math.BigDecimal;

/**
 * @author gongshuiwen
 */
@Getter
public class BigDecimalFieldDefinition extends FieldDefinition {

    private final BigDecimal defaultValue;

    public BigDecimalFieldDefinition(Field field, BigDecimalField bigDecimalFieldAnnotation) {
        super(field, FieldType.BIG_DECIMAL,
                bigDecimalFieldAnnotation.name(),
                bigDecimalFieldAnnotation.description(),
                bigDecimalFieldAnnotation.requiredMode(),
                bigDecimalFieldAnnotation.accessMode(),
                bigDecimalFieldAnnotation.deprecated(),
                bigDecimalFieldAnnotation.persistent(),
                bigDecimalFieldAnnotation.persistName(),
                bigDecimalFieldAnnotation.nullable(),
                bigDecimalFieldAnnotation.defaultNull());

        this.defaultValue = new BigDecimal(bigDecimalFieldAnnotation.defaultValue());
    }
}
