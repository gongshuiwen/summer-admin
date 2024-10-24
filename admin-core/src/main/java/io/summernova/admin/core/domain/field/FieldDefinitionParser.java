package io.summernova.admin.core.domain.field;

import java.lang.reflect.Field;

/**
 * @author gongshuiwen
 */
public interface FieldDefinitionParser<T extends FieldDefinition> {

    boolean check(Field field);

    T parse(Field field);
}
