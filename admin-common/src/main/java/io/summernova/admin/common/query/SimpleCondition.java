package io.summernova.admin.common.query;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * @author gongshuiwen
 */
@Slf4j
@Getter
public final class SimpleCondition extends Condition {

    private final String field;
    private final Object value;

    // prevent external instantiation
    private SimpleCondition(String field, String operator, Object value) {
        super(operator);
        this.field = field;
        this.value = value;
    }

    public static SimpleCondition of(String field, GeneralOperator operator, Object value) {
        checkField(field);
        checkValue(value);
        return new SimpleCondition(field, operator.getName(), value);
    }

    public static SimpleCondition of(String field, LikeOperator operator, Object value) {
        checkField(field);
        checkValue(value);
        return new SimpleCondition(field, operator.toString(), value);
    }

    public static SimpleCondition of(String field, String operator, Object value) {
        checkField(field);
        checkOperator(operator);
        checkValue(value);
        return new SimpleCondition(field, operator, value);
    }

    private static void checkOperator(String operator) {
        Objects.requireNonNull(operator, "operator must not be null");
        if (operator.isBlank()) {
            throw new IllegalArgumentException("operator must not be blank");
        }
        if (!GeneralOperator.contains(operator) && !LikeOperator.contains(operator)) {
            throw new IllegalArgumentException("Unsupported operator: " + operator);
        }
    }

    private static void checkValue(Object value) {
        // TODO: check value valid depends on field type and operator
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
}