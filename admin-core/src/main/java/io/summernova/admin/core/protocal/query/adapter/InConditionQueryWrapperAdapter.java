package io.summernova.admin.core.protocal.query.adapter;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.summernova.admin.common.query.InCondition;
import lombok.Getter;

import static io.summernova.admin.common.query.InCondition.OPERATOR_IN;
import static io.summernova.admin.common.query.InCondition.OPERATOR_NOT_IN;

/**
 * @author gongshuiwen
 */
@Getter
final class InConditionQueryWrapperAdapter implements ConditionQueryWrapperAdapter<InCondition> {

    @Override
    public <M> void applyToQueryWrapper(InCondition inCondition, QueryWrapper<M> queryWrapper) {
        String operator = inCondition.getOperator();
        if (OPERATOR_IN.equals(operator)) {
            queryWrapper.in(inCondition.getField(), inCondition.getValues());
        } else if (OPERATOR_NOT_IN.equals(operator)) {
            queryWrapper.notIn(inCondition.getField(), inCondition.getValues());
        } else {
            throw new IllegalArgumentException("Unsupported operator '" + operator + "' for InCondition.");
        }
    }
}
