package io.summernova.admin.core.query.adapter;

import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.enums.SqlKeyword;
import io.summernova.admin.common.query.GeneralCondition;
import io.summernova.admin.common.query.GeneralOperator;
import lombok.Getter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author gongshuiwen
 */
@Getter
final class GeneralConditionQueryWrapperAdapter implements ConditionQueryWrapperAdapter<GeneralCondition> {

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
    public <M> void applyToQueryWrapper(GeneralCondition generalCondition, QueryWrapper<M> queryWrapper) {
        try {
            METHOD_ADD_CONDITION_.invoke(queryWrapper, true, generalCondition.getField(),
                    getSqlKeyword(generalCondition.getOperator()), generalCondition.getValue());
        } catch (InvocationTargetException | IllegalAccessException e) {
            // TODO: Handle reflection exceptions
            throw new RuntimeException(e);
        }
    }

    private SqlKeyword getSqlKeyword(String generalOperator) {
        if (generalOperator.equals(GeneralOperator.EQ.getName())) {
            return SqlKeyword.EQ;
        } else if (generalOperator.equals(GeneralOperator.NE.getName())) {
            return SqlKeyword.NE;
        } else if (generalOperator.equals(GeneralOperator.LT.getName())) {
            return SqlKeyword.LT;
        } else if (generalOperator.equals(GeneralOperator.GT.getName())) {
            return SqlKeyword.GT;
        } else if (generalOperator.equals(GeneralOperator.LE.getName())) {
            return SqlKeyword.LE;
        } else if (generalOperator.equals(GeneralOperator.GE.getName())) {
            return SqlKeyword.GE;
        } else {
            throw new IllegalArgumentException("Unsupported general operator: " + generalOperator);
        }
    }
}