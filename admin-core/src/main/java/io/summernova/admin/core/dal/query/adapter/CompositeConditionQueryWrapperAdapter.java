package io.summernova.admin.core.dal.query.adapter;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.summernova.admin.common.query.CompositeCondition;
import io.summernova.admin.common.query.CompositeOperator;
import io.summernova.admin.common.query.Condition;
import lombok.Getter;

/**
 * @author gongshuiwen
 */
@Getter
final class CompositeConditionQueryWrapperAdapter implements ConditionQueryWrapperAdapter<CompositeCondition> {

    @Override
    public <M> void applyToQueryWrapper(CompositeCondition condition, QueryWrapper<M> queryWrapper) {
        String operator = condition.getOperator();
        Condition[] conditions = condition.getConditions();
        if (operator.equals(CompositeOperator.AND.getName())) {
            for (Condition subCondition : conditions)
                queryWrapper.and(queryWrapper1 ->
                        ConditionQueryWrapperAdapter.applyConditionToQueryWrapper(subCondition, queryWrapper1));
        } else if (operator.equals(CompositeOperator.OR.getName())) {
            for (Condition subCondition : conditions)
                queryWrapper.or(queryWrapper1 ->
                        ConditionQueryWrapperAdapter.applyConditionToQueryWrapper(subCondition, queryWrapper1));
        } else if (operator.equals(CompositeOperator.NOT.getName())) {
            Condition subCondition = conditions[0];
            queryWrapper.not(queryWrapper1 ->
                    ConditionQueryWrapperAdapter.applyConditionToQueryWrapper(subCondition, queryWrapper1));
        } else {
            throw new IllegalArgumentException("Unsupported composite operator: " + operator);
        }
    }
}