package io.summernova.admin.core.dal.query.adapter;

import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.enums.SqlKeyword;
import io.summernova.admin.common.query.CompareCondition;
import io.summernova.admin.common.query.CompareOperator;
import lombok.Getter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author gongshuiwen
 */
@Getter
final class CompareConditionQueryWrapperAdapter implements ConditionQueryWrapperAdapter<CompareCondition> {

    // cache method addCondition of AbstractWrapper
    private static final String METHOD_ADD_CONDITION = "addCondition";
    private static final Method METHOD_ADD_CONDITION_;

    static {
        try {
            METHOD_ADD_CONDITION_ = AbstractWrapper.class.getDeclaredMethod(METHOD_ADD_CONDITION,
                    boolean.class, Object.class, SqlKeyword.class, Object.class);
            METHOD_ADD_CONDITION_.setAccessible(true);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <M> void applyToQueryWrapper(CompareCondition compareCondition, QueryWrapper<M> queryWrapper) {
        try {
            METHOD_ADD_CONDITION_.invoke(queryWrapper, true, compareCondition.getField(),
                    getSqlKeyword(compareCondition.getOperator()), compareCondition.getValue());
        } catch (InvocationTargetException | IllegalAccessException e) {
            // TODO: Handle reflection exceptions
            throw new RuntimeException(e);
        }
    }

    private SqlKeyword getSqlKeyword(String compareOperator) {
        if (compareOperator.equals(CompareOperator.EQ.getName())) {
            return SqlKeyword.EQ;
        } else if (compareOperator.equals(CompareOperator.NE.getName())) {
            return SqlKeyword.NE;
        } else if (compareOperator.equals(CompareOperator.LT.getName())) {
            return SqlKeyword.LT;
        } else if (compareOperator.equals(CompareOperator.GT.getName())) {
            return SqlKeyword.GT;
        } else if (compareOperator.equals(CompareOperator.LE.getName())) {
            return SqlKeyword.LE;
        } else if (compareOperator.equals(CompareOperator.GE.getName())) {
            return SqlKeyword.GE;
        } else {
            throw new IllegalArgumentException("Unsupported general operator: " + compareOperator);
        }
    }
}