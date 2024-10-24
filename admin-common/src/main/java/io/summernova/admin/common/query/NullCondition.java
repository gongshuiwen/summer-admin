package io.summernova.admin.common.query;

import lombok.Getter;

/**
 * @author gongshuiwen
 */
@Getter
public final class NullCondition extends Condition {

    public static final String OPERATOR_IS_NULL = "isNull";
    public static final String OPERATOR_IS_NOT_NULL = "isNotNull";

    private final String field;

    // prevent external instantiation
    private NullCondition(String operator, String field) {
        super(operator);
        this.field = field;
    }

    // ------------------------
    // Static factory methods
    // ------------------------
    public static NullCondition isNull(String field) {
        checkField(field);
        return new NullCondition(OPERATOR_IS_NULL, field);
    }

    public static NullCondition isNotNull(String field) {
        checkField(field);
        return new NullCondition(OPERATOR_IS_NOT_NULL, field);
    }

    @Override
    public String getSql() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
