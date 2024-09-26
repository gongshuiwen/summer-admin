package io.summernova.admin.core.dal.query.adapter;

import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.enums.SqlKeyword;
import com.baomidou.mybatisplus.core.enums.SqlLike;
import io.summernova.admin.common.query.LikeCondition;
import io.summernova.admin.common.query.LikeOperator;
import lombok.Getter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author gongshuiwen
 */
@Getter
final class LikeConditionQueryWrapperAdapter implements ConditionQueryWrapperAdapter<LikeCondition> {

    // cache method likeValue of AbstractWrapper
    private static final String LIKE_VALUE_METHOD_NAME = "likeValue";
    private static final Method LIKE_VALUE_METHOD;

    static {
        try {
            LIKE_VALUE_METHOD = AbstractWrapper.class.getDeclaredMethod(LIKE_VALUE_METHOD_NAME,
                    boolean.class, SqlKeyword.class, Object.class, Object.class, SqlLike.class);
            LIKE_VALUE_METHOD.setAccessible(true);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <M> void applyToQueryWrapper(LikeCondition likeCondition, QueryWrapper<M> queryWrapper) {
        SqlKeyWordAndSqlLike sqlKeyWordAndSqlLike = getSqlLike(likeCondition.getOperator());
        try {
            LIKE_VALUE_METHOD.invoke(queryWrapper, true, sqlKeyWordAndSqlLike.sqlKeyword,
                    likeCondition.getField(), likeCondition.getValue(), sqlKeyWordAndSqlLike.sqlLike);
        } catch (InvocationTargetException | IllegalAccessException e) {
            // TODO: Handle reflection exceptions
            throw new RuntimeException(e);
        }
    }

    private SqlKeyWordAndSqlLike getSqlLike(String likeOperator) {
        if (likeOperator.equals(LikeOperator.LIKE.getName())) {
            return new SqlKeyWordAndSqlLike(SqlKeyword.LIKE, SqlLike.DEFAULT);
        } else if (likeOperator.equals(LikeOperator.LIKE_LEFT.getName())) {
            return new SqlKeyWordAndSqlLike(SqlKeyword.LIKE, SqlLike.LEFT);
        } else if (likeOperator.equals(LikeOperator.LIKE_RIGHT.getName())) {
            return new SqlKeyWordAndSqlLike(SqlKeyword.LIKE, SqlLike.RIGHT);
        } else if (likeOperator.equals(LikeOperator.NOT_LIKE.getName())) {
            return new SqlKeyWordAndSqlLike(SqlKeyword.NOT_LIKE, SqlLike.DEFAULT);
        } else if (likeOperator.equals(LikeOperator.NOT_LIKE_LEFT.getName())) {
            return new SqlKeyWordAndSqlLike(SqlKeyword.NOT_LIKE, SqlLike.LEFT);
        } else if (likeOperator.equals(LikeOperator.NOT_LIKE_RIGHT.getName())) {
            return new SqlKeyWordAndSqlLike(SqlKeyword.NOT_LIKE, SqlLike.RIGHT);
        } else {
            throw new IllegalArgumentException("Unsupported like operator: '" + likeOperator);
        }
    }

    record SqlKeyWordAndSqlLike(SqlKeyword sqlKeyword, SqlLike sqlLike) {
    }
}