package com.hzboiler.erp.core.protocal.query;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hzboiler.erp.core.model.BaseModel;
import lombok.Getter;

import java.util.Objects;

/**
 * @author gongshuiwen
 */
@Getter
public final class BetweenCondition extends Condition {

    private static final String OPERATOR_BETWEEN = "between";
    private static final String OPERATOR_NOT_BETWEEN = "notBetween";

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
    public void applyToQueryWrapper(QueryWrapper<? extends BaseModel> queryWrapper) {
        if (OPERATOR_BETWEEN.equals(operator)) {
            queryWrapper.between(field, value1, value2);
        } else if (OPERATOR_NOT_BETWEEN.equals(operator)) {
            queryWrapper.notBetween(field, value1, value2);
        } else {
            throw new IllegalArgumentException("Unsupported operator '" + operator + "' for BetweenCondition.");
        }
    }

    @Override
    public String getSql() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
