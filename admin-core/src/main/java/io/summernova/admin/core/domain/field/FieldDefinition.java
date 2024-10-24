package io.summernova.admin.core.domain.field;

import io.summernova.admin.core.domain.annotations.AccessMode;
import io.summernova.admin.core.domain.annotations.RequiredMode;
import lombok.Getter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author gongshuiwen
 */
@Getter
public abstract class FieldDefinition {

    private static final List<FieldDefinitionParser<?>> fieldDefinitionParsers = new ArrayList<>();

    static {
        fieldDefinitionParsers.add(new BooleanFieldDefinitionParser());
        fieldDefinitionParsers.add(new ShortFieldDefinitionParser());
        fieldDefinitionParsers.add(new IntegerFieldDefinitionParser());
        fieldDefinitionParsers.add(new LongFieldDefinitionParser());
        fieldDefinitionParsers.add(new BigDecimalFieldDefinitionParser());
        fieldDefinitionParsers.add(new StringFieldDefinitionParser());
        fieldDefinitionParsers.add(new DateFieldDefinitionParser());
        fieldDefinitionParsers.add(new TimeFieldDefinitionParser());
        fieldDefinitionParsers.add(new DateTimeFieldDefinitionParser());
    }

    private final Field field;
    private final FieldType fieldType;
    private final String name;
    private final String description;
    private final RequiredMode requiredMode;
    private final AccessMode accessMode;
    private final boolean deprecated;
    private final boolean persistent;
    private final String persistName;
    private final boolean nullable;
    private final boolean defaultNull;

    public FieldDefinition(
            final Field field, final FieldType fieldType, final String name, final String description,
            final RequiredMode requiredMode, final AccessMode accessMode, final boolean deprecated,
            final boolean persistent, final String persistName,
            final boolean nullable, final boolean defaultNull) {
        this.field = field;
        this.fieldType = fieldType;
        this.description = description;
        this.requiredMode = requiredMode;
        this.accessMode = accessMode;
        this.deprecated = deprecated;
        this.persistent = persistent;
        this.nullable = nullable;
        this.defaultNull = defaultNull;

        this.name = "".equals(name) ? field.getName() : name;
        this.persistName = "".equals(persistName) ? this.name : persistName;
    }

    public static FieldDefinition parseFieldDefinition(final Field field) {
        FieldDefinition fieldDefinition = null;
        for (FieldDefinitionParser<?> parser : fieldDefinitionParsers) {
            if (parser.check(field)) {
                fieldDefinition = parser.parse(field);
                break;
            }
        }

        if (fieldDefinition == null)
            throw new RuntimeException("Cannot parse field definition of field '" + field.getName() + "'.");

        return fieldDefinition;
    }
}
