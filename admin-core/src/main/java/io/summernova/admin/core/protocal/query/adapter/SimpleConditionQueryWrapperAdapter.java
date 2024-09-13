package io.summernova.admin.core.protocal.query.adapter;

import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.enums.SqlKeyword;
import com.baomidou.mybatisplus.core.enums.SqlLike;
import io.summernova.admin.core.protocal.query.GeneralOperator;
import io.summernova.admin.core.protocal.query.LikeOperator;
import io.summernova.admin.core.protocal.query.SimpleCondition;
import lombok.Getter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author gongshuiwen
 */
@Getter
final class SimpleConditionQueryWrapperAdapter implements ConditionQueryWrapperAdapter<SimpleCondition> {

    // cache method addCondition and likeValue of AbstractWrapper
    private static final Method methodAddCondition;
    private static final Method methodLikeValue;

    static {
        try {
            methodAddCondition = AbstractWrapper.class.getDeclaredMethod("addCondition",
                    boolean.class, Object.class, SqlKeyword.class, Object.class);
            methodLikeValue = AbstractWrapper.class.getDeclaredMethod("likeValue",
                    boolean.class, SqlKeyword.class, Object.class, Object.class, SqlLike.class);
            methodAddCondition.setAccessible(true);
            methodLikeValue.setAccessible(true);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <M> void applyToQueryWrapper(SimpleCondition simpleCondition, QueryWrapper<M> queryWrapper) {
        String operator = simpleCondition.getOperator();
        if (GeneralOperator.contains(operator)) {
            applyGeneral(operator, simpleCondition.getField(), simpleCondition.getValue(), queryWrapper);
        } else if (LikeOperator.contains(operator)) {
            applyLike(operator, simpleCondition.getField(), simpleCondition.getValue(), queryWrapper);
        } else {
            throw new IllegalArgumentException("Unsupported operator '" + operator + "' for SimpleCondition.");
        }
    }

    private void applyGeneral(String operator, String field, Object value, QueryWrapper<?> queryWrapper) {
        SqlKeyword sqlKeyword = GeneralOperator.get(operator).getSqlKeyword();
        try {
            methodAddCondition.invoke(queryWrapper, true, field, sqlKeyword, value);
        } catch (InvocationTargetException | IllegalAccessException e) {
            // TODO: Handle reflection exceptions
            throw new RuntimeException(e);
        }
    }

    private void applyLike(String operator, String field, Object value, QueryWrapper<?> queryWrapper) {
        LikeOperator likeOperator = LikeOperator.of(operator);
        try {
            methodLikeValue.invoke(queryWrapper, true, likeOperator.getSqlKeyword(), field, value, likeOperator.getSqlLike());
        } catch (InvocationTargetException | IllegalAccessException e) {
            // TODO: Handle reflection exceptions
            throw new RuntimeException(e);
        }
    }
}