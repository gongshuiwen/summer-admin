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
        SqlKeyword sqlKeyword = getSqlKeyword(GeneralOperator.get(operator));
        try {
            methodAddCondition.invoke(queryWrapper, true, field, sqlKeyword, value);
        } catch (InvocationTargetException | IllegalAccessException e) {
            // TODO: Handle reflection exceptions
            throw new RuntimeException(e);
        }
    }

    private SqlKeyword getSqlKeyword(GeneralOperator generalOperator) {
        if (generalOperator == GeneralOperator.EQ) {
            return SqlKeyword.EQ;
        } else if (generalOperator == GeneralOperator.LT) {
            return SqlKeyword.LT;
        } else if (generalOperator == GeneralOperator.GT) {
            return SqlKeyword.GT;
        } else if (generalOperator == GeneralOperator.NE) {
            return SqlKeyword.NE;
        } else if (generalOperator == GeneralOperator.LE) {
            return SqlKeyword.LE;
        } else if (generalOperator == GeneralOperator.GE) {
            return SqlKeyword.GE;
        }
        throw new IllegalArgumentException("Unsupported operator '" + generalOperator + "' for SimpleCondition.");
    }

    private void applyLike(String operator, String field, Object value, QueryWrapper<?> queryWrapper) {
        LikeOperator likeOperator = LikeOperator.of(operator);
        try {
            methodLikeValue.invoke(queryWrapper, true, getSqlKeyword(likeOperator), field, value, getSqlLike(likeOperator));
        } catch (InvocationTargetException | IllegalAccessException e) {
            // TODO: Handle reflection exceptions
            throw new RuntimeException(e);
        }
    }

    private SqlKeyword getSqlKeyword(LikeOperator likeOperator) {
        if (likeOperator == LikeOperator.LIKE) {
            return SqlKeyword.LIKE;
        } else if (likeOperator == LikeOperator.LIKE_LEFT) {
            return SqlKeyword.LIKE;
        } else if (likeOperator == LikeOperator.LIKE_RIGHT) {
            return SqlKeyword.LIKE;
        } else if (likeOperator == LikeOperator.NOT_LIKE) {
            return SqlKeyword.NOT_LIKE;
        } else if (likeOperator == LikeOperator.NOT_LIKE_LEFT) {
            return SqlKeyword.NOT_LIKE;
        } else if (likeOperator == LikeOperator.NOT_LIKE_RIGHT) {
            return SqlKeyword.NOT_LIKE;
        }
        throw new IllegalArgumentException("Unsupported operator '" + likeOperator + "' for SimpleCondition.");
    }

    private SqlLike getSqlLike(LikeOperator likeOperator) {
        if (likeOperator == LikeOperator.LIKE) {
            return SqlLike.DEFAULT;
        } else if (likeOperator == LikeOperator.LIKE_LEFT) {
            return SqlLike.LEFT;
        } else if (likeOperator == LikeOperator.LIKE_RIGHT) {
            return SqlLike.RIGHT;
        } else if (likeOperator == LikeOperator.NOT_LIKE) {
            return SqlLike.DEFAULT;
        } else if (likeOperator == LikeOperator.NOT_LIKE_LEFT) {
            return SqlLike.LEFT;
        } else if (likeOperator == LikeOperator.NOT_LIKE_RIGHT) {
            return SqlLike.RIGHT;
        }
        throw new IllegalArgumentException("Unsupported operator '" + likeOperator + "' for SimpleCondition.");
    }
}