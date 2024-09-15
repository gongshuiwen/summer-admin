package io.summernova.admin.common.query;

import lombok.Getter;

import java.util.List;

/**
 * @author gongshuiwen
 */
@Getter
public final class InCondition extends Condition {

    public static final String OPERATOR_IN = "in";
    public static final String OPERATOR_NOT_IN = "notIn";

    private final String field;
    private final Object[] values;

    // prevent external instantiation
    private InCondition(String operator, String field, List<?> values) {
        super(operator);
        this.field = field;
        this.values = values.toArray(new Object[0]);
    }

    public static InCondition in(String field, List<?> values) {
        checkField(field);
        checkValues(values);
        return new InCondition(OPERATOR_IN, field, values);
    }

    public static InCondition notIn(String field, List<?> values) {
        checkField(field);
        checkValues(values);
        return new InCondition(OPERATOR_NOT_IN, field, values);
    }

    private static void checkValues(List<?> values) {
        if (values == null || values.isEmpty())
            throw new IllegalArgumentException("Values cannot be null or empty.");
    }

    public String getSql() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
