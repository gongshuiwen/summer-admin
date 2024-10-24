package io.summernova.admin.core.domain.field;

import lombok.Getter;

/**
 * @author gongshuiwen
 */
@Getter
public enum FieldType {
    BOOLEAN("boolean"),
    SHORT("short"),
    INTEGER("integer"),
    LONG("long"),
    BIG_DECIMAL("bigDecimal"),
    STRING("string"),
    ENUM("enum"),
    DATE("date"),
    TIME("time"),
    DATETIME("datetime"),
    ONE2MANY("one2many"),
    MANY2MANY("many2many"),
    MANY2ONE("many2one"),
    CUSTOM("custom");

    private final String name;

    FieldType(String name) {
        this.name = name;
    }
}