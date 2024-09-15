package io.summernova.admin.common.query;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author gongshuiwen
 */
@Slf4j
@Getter
public final class CompareCondition extends Condition {

    private final String field;
    private final Object value;

    // prevent external instantiation
    private CompareCondition(String field, CompareOperator compareOperator, Object value) {
        super(compareOperator.getName());
        this.field = field;
        this.value = value;
    }

    // -------------------------
    // Static factory methods
    // -------------------------
    public static CompareCondition of(String field, CompareOperator compareOperator, Object value) {
        checkField(field);
        return new CompareCondition(field, compareOperator, value);
    }

    public static CompareCondition of(String field, String compareOperator, Object value) {
        return new CompareCondition(field, CompareOperator.of(compareOperator), value);
    }

    public static CompareCondition eq(String field, Object value) {
        checkField(field);
        return new CompareCondition(field, CompareOperator.EQ, value);
    }

    public static CompareCondition ne(String field, Object value) {
        checkField(field);
        return new CompareCondition(field, CompareOperator.NE, value);
    }

    public static CompareCondition lt(String field, Object value) {
        checkField(field);
        return new CompareCondition(field, CompareOperator.LT, value);
    }

    public static CompareCondition gt(String field, Object value) {
        checkField(field);
        return new CompareCondition(field, CompareOperator.GT, value);
    }

    public static CompareCondition le(String field, Object value) {
        checkField(field);
        return new CompareCondition(field, CompareOperator.LE, value);
    }

    public static CompareCondition ge(String field, Object value) {
        checkField(field);
        return new CompareCondition(field, CompareOperator.GE, value);
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