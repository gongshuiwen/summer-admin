package com.hzboiler.erp.core.protocal.query;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hzboiler.erp.core.model.BaseModel;
import lombok.Getter;

/**
 * @author gongshuiwen
 */
@Getter
public final class NullCondition extends Condition {

    private static final String OPERATOR_IS_NULL = "isNull";
    private static final String OPERATOR_IS_NOT_NULL = "isNotNull";

    private final String field;

    private NullCondition(String operator, String field) {
        super(operator);
        this.field = field;
    }

    public static NullCondition isNull(String field) {
        checkField(field);
        return new NullCondition(OPERATOR_IS_NULL, field);
    }

    public static NullCondition isNotNull(String field) {
        checkField(field);
        return new NullCondition(OPERATOR_IS_NOT_NULL, field);
    }

    @Override
    public void applyToQueryWrapper(QueryWrapper<? extends BaseModel> queryWrapper) {
        if (OPERATOR_IS_NULL.equals(operator)) {
            queryWrapper.isNull(field);
        } else if (OPERATOR_IS_NOT_NULL.equals(operator)) {
            queryWrapper.isNotNull(field);
        } else {
            throw new IllegalArgumentException("Unsupported operator '" + operator + "' for NullCondition.");
        }
    }

    @Override
    public String getSql() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
