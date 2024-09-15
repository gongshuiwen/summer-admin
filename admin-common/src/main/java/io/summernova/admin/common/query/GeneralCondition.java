package io.summernova.admin.common.query;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author gongshuiwen
 */
@Slf4j
@Getter
public final class GeneralCondition extends Condition {

    private final String field;
    private final Object value;

    // prevent external instantiation
    private GeneralCondition(String field, GeneralOperator generalOperator, Object value) {
        super(generalOperator.getName());
        this.field = field;
        this.value = value;
    }

    // -------------------------
    // Static factory methods
    // -------------------------
    public static GeneralCondition of(String field, GeneralOperator generalOperator, Object value) {
        checkField(field);
        return new GeneralCondition(field, generalOperator, value);
    }

    public static GeneralCondition of(String field, String generalOperator, Object value) {
        return new GeneralCondition(field, GeneralOperator.of(generalOperator), value);
    }

    public static GeneralCondition eq(String field, Object value) {
        checkField(field);
        return new GeneralCondition(field, GeneralOperator.EQ, value);
    }

    public static GeneralCondition ne(String field, Object value) {
        checkField(field);
        return new GeneralCondition(field, GeneralOperator.NE, value);
    }

    public static GeneralCondition lt(String field, Object value) {
        checkField(field);
        return new GeneralCondition(field, GeneralOperator.LT, value);
    }

    public static GeneralCondition gt(String field, Object value) {
        checkField(field);
        return new GeneralCondition(field, GeneralOperator.GT, value);
    }

    public static GeneralCondition le(String field, Object value) {
        checkField(field);
        return new GeneralCondition(field, GeneralOperator.LE, value);
    }

    public static GeneralCondition ge(String field, Object value) {
        checkField(field);
        return new GeneralCondition(field, GeneralOperator.GE, value);
    }

    @Override
    public String getSql() {
        // TODO: SQL injection protection
        log.warn("!!! This is an experimental feature, which can lead to SQL injection risks, " +
                "so please use it with a clear understanding of how to avoid that risk.");
        if (value instanceof String) {
            return field + " " + operator.toUpperCase() + " '" + value + "'";
        }
        return field + " " + operator.toUpperCase() + " " + value;
    }

    @Override
    public String toString() {
        if (value instanceof String)
            return field + " " + operator.toUpperCase() + " '" + value + "'";
        return field + " " + operator.toUpperCase() + " " + value;
    }
}