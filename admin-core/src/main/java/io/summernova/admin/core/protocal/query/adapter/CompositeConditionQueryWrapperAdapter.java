package io.summernova.admin.core.protocal.query.adapter;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.summernova.admin.core.protocal.query.CompositeCondition;
import io.summernova.admin.core.protocal.query.Condition;
import lombok.Getter;

import java.util.List;

import static io.summernova.admin.core.protocal.query.CompositeCondition.*;

/**
 * @author gongshuiwen
 */
@Getter
final class CompositeConditionQueryWrapperAdapter implements ConditionQueryWrapperAdapter<CompositeCondition> {

    @Override
    public <M> void applyToQueryWrapper(CompositeCondition condition, QueryWrapper<M> queryWrapper) {
        String operator = condition.getOperator();
        List<Condition> conditions = condition.getConditions();
        if (OPERATOR_AND.equals(operator)) {
            conditions.forEach(subCondition -> queryWrapper.and(
                    queryWrapper1 -> ConditionQueryWrapperAdapter.applyConditionToQueryWrapper(subCondition, queryWrapper1)));
        } else if (OPERATOR_OR.equals(operator)) {
            conditions.forEach(subCondition -> queryWrapper.or(
                    queryWrapper1 -> ConditionQueryWrapperAdapter.applyConditionToQueryWrapper(subCondition, queryWrapper1)));
        } else if (OPERATOR_NOT.equals(operator)) {
            conditions.forEach(subCondition -> queryWrapper.not(
                    queryWrapper1 -> ConditionQueryWrapperAdapter.applyConditionToQueryWrapper(subCondition, queryWrapper1)));
        } else {
            throw new IllegalArgumentException("Unsupported operator '" + operator + "' for CompositeCondition.");
        }
    }
}