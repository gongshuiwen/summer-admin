package io.summernova.admin.core.protocal.query.adapter;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.summernova.admin.common.query.BetweenCondition;
import lombok.Getter;

import static io.summernova.admin.common.query.BetweenCondition.OPERATOR_BETWEEN;
import static io.summernova.admin.common.query.BetweenCondition.OPERATOR_NOT_BETWEEN;

/**
 * @author gongshuiwen
 */
@Getter
final class BetweenConditionQueryWrapperAdapter implements ConditionQueryWrapperAdapter<BetweenCondition> {

    @Override
    public <M> void applyToQueryWrapper(BetweenCondition condition, QueryWrapper<M> queryWrapper) {
        final String operator = condition.getOperator();
        if (OPERATOR_BETWEEN.equals(operator)) {
            queryWrapper.between(condition.getField(), condition.getValue1(), condition.getValue2());
        } else if (OPERATOR_NOT_BETWEEN.equals(operator)) {
            queryWrapper.notBetween(condition.getField(), condition.getValue1(), condition.getValue2());
        } else {
            throw new IllegalArgumentException("Unsupported operator '" + operator + "' for BetweenCondition.");
        }
    }
}
