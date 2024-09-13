package io.summernova.admin.core.protocal.query.adapter;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.summernova.admin.core.protocal.query.NullCondition;
import lombok.Getter;

import static io.summernova.admin.core.protocal.query.NullCondition.OPERATOR_IS_NOT_NULL;
import static io.summernova.admin.core.protocal.query.NullCondition.OPERATOR_IS_NULL;

/**
 * @author gongshuiwen
 */
@Getter
final class NullConditionQueryWrapperAdapter implements ConditionQueryWrapperAdapter<NullCondition> {

    @Override
    public <M> void applyToQueryWrapper(NullCondition nullCondition, QueryWrapper<M> queryWrapper) {
        String operator = nullCondition.getOperator();
        if (OPERATOR_IS_NULL.equals(operator)) {
            queryWrapper.isNull(nullCondition.getField());
        } else if (OPERATOR_IS_NOT_NULL.equals(operator)) {
            queryWrapper.isNotNull(nullCondition.getField());
        } else {
            throw new IllegalArgumentException("Unsupported operator '" + operator + "' for NullCondition.");
        }
    }
}
