package io.summernova.admin.core.protocal.query;

import lombok.Getter;

import java.util.Objects;

/**
 * @author gongshuiwen
 */
@Getter
public final class BetweenCondition extends Condition {

    public static final String OPERATOR_BETWEEN = "between";
    public static final String OPERATOR_NOT_BETWEEN = "notBetween";

    private final String field;
    private final Object value1;
    private final Object value2;

    // prevent external instantiation
    private BetweenCondition(String operator, String field, Object value1, Object value2) {
        super(operator);
        this.field = field;
        this.value1 = value1;
        this.value2 = value2;
    }

    public static BetweenCondition between(String field, Object value1, Object value2) {
        checkField(field);
        checkValue(value1, value2);
        return new BetweenCondition(OPERATOR_BETWEEN, field, value1, value2);
    }

    public static BetweenCondition notBetween(String field, Object value1, Object value2) {
        checkField(field);
        checkValue(value1, value2);
        return new BetweenCondition(OPERATOR_NOT_BETWEEN, field, value1, value2);
    }

    private static void checkValue(Object value1, Object value2) {
        Objects.requireNonNull(value1, "value1 must not be null");
        Objects.requireNonNull(value2, "value2 must not be null");
    }

    @Override
    public String getSql() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
