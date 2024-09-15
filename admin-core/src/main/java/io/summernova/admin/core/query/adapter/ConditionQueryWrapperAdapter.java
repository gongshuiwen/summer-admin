package io.summernova.admin.core.query.adapter;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.summernova.admin.common.query.*;

/**
 * @author gongshuiwen
 */
public interface ConditionQueryWrapperAdapter<T extends Condition> extends QueryWrapperAdapter<T> {

    BetweenConditionQueryWrapperAdapter BETWEEN_CONDITION_QUERY_WRAPPER_ADAPTER = new BetweenConditionQueryWrapperAdapter();
    CompositeConditionQueryWrapperAdapter COMPOSITE_CONDITION_QUERY_WRAPPER_ADAPTER = new CompositeConditionQueryWrapperAdapter();
    GeneralConditionQueryWrapperAdapter GENERAL_CONDITION_QUERY_WRAPPER_ADAPTER = new GeneralConditionQueryWrapperAdapter();
    InConditionQueryWrapperAdapter IN_CONDITION_QUERY_WRAPPER_ADAPTER = new InConditionQueryWrapperAdapter();
    LikeConditionQueryWrapperAdapter LIKE_CONDITION_QUERY_WRAPPER_ADAPTER = new LikeConditionQueryWrapperAdapter();
    NullConditionQueryWrapperAdapter NULL_CONDITION_QUERY_WRAPPER_ADAPTER = new NullConditionQueryWrapperAdapter();

    static <M> QueryWrapper<M> transformConditionToQueryWrapper(Condition condition, Class<M> modelClass) {
        QueryWrapper<M> queryWrapper = new QueryWrapper<>(modelClass);
        applyConditionToQueryWrapper(condition, queryWrapper);
        return queryWrapper;
    }

    static void applyConditionToQueryWrapper(Condition condition, QueryWrapper<?> queryWrapper) {
        if (condition == null)
            return;

        if (condition instanceof BetweenCondition betweenCondition) {
            BETWEEN_CONDITION_QUERY_WRAPPER_ADAPTER.applyToQueryWrapper(betweenCondition, queryWrapper);
        } else if (condition instanceof CompositeCondition compositeCondition) {
            COMPOSITE_CONDITION_QUERY_WRAPPER_ADAPTER.applyToQueryWrapper(compositeCondition, queryWrapper);
        } else if (condition instanceof GeneralCondition generalCondition) {
            GENERAL_CONDITION_QUERY_WRAPPER_ADAPTER.applyToQueryWrapper(generalCondition, queryWrapper);
        } else if (condition instanceof InCondition inCondition) {
            IN_CONDITION_QUERY_WRAPPER_ADAPTER.applyToQueryWrapper(inCondition, queryWrapper);
        } else if (condition instanceof LikeCondition likeCondition) {
            LIKE_CONDITION_QUERY_WRAPPER_ADAPTER.applyToQueryWrapper(likeCondition, queryWrapper);
        } else if (condition instanceof NullCondition nullCondition) {
            NULL_CONDITION_QUERY_WRAPPER_ADAPTER.applyToQueryWrapper(nullCondition, queryWrapper);
        } else {
            throw new IllegalArgumentException("Unsupported condition type: " + condition.getClass().getName());
        }
    }
}
