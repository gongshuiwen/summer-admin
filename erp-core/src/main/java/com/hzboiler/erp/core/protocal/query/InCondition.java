package com.hzboiler.erp.core.protocal.query;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hzboiler.erp.core.model.BaseModel;

import java.util.List;

/**
 * @author gongshuiwen
 */
public final class InCondition extends Condition {

    private static final String OPERATOR_IN = "in";
    private static final String OPERATOR_NOT_IN = "notIn";

    private final String field;
    private final List<Object> values;

    // prevent external instantiation
    private InCondition(String operator, String field, List<Object> values) {
        super(operator);
        this.field = field;
        this.values = values;
    }

    public static InCondition in(String field, List<Object> values) {
        checkField(field);
        checkValues(values);
        return new InCondition(OPERATOR_IN, field, values);
    }

    public static InCondition notIn(String field, List<Object> values) {
        checkField(field);
        checkValues(values);
        return new InCondition(OPERATOR_NOT_IN, field, values);
    }

    private static void checkValues(List<Object> values) {
        if (values == null || values.isEmpty())
            throw new IllegalArgumentException("Values cannot be null or empty.");
    }

    public void applyToQueryWrapper(QueryWrapper<? extends BaseModel> queryWrapper) {
        if (OPERATOR_IN.equals(operator)) {
            queryWrapper.in(field, values);
        } else if (OPERATOR_NOT_IN.equals(operator)) {
            queryWrapper.notIn(field, values);
        } else {
            throw new IllegalArgumentException("Unsupported operator '" + operator + "' for InCondition.");
        }
    }

    public String getSql() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
