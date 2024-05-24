package com.hzboiler.core.protocal;

import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.enums.SqlKeyword;
import com.baomidou.mybatisplus.core.enums.SqlLike;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author gongshuiwen
 */
@Setter
@Getter
public class Condition<T> {

    private static final Method generalMethod;
    private static final Method likeMethod;
    static {
        try {
            generalMethod = AbstractWrapper.class.getDeclaredMethod("addCondition",
                    boolean.class, Object.class, SqlKeyword.class, Object.class);
            likeMethod = AbstractWrapper.class.getDeclaredMethod("likeValue",
                    boolean.class, SqlKeyword.class, Object.class, Object.class, SqlLike.class);
            generalMethod.setAccessible(true);
            likeMethod.setAccessible(true);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    String column;
    String operator;
    Object value;
    private List<Condition<T>> conditions;

    public Condition(String column, String operator, Object value) {
        this.column = column;
        this.operator = operator;
        this.value = value;
    }

    public Condition(String operator, List<Condition<T>> conditions) {
        this.operator = operator;
        this.conditions = conditions;
    }

    public QueryWrapper<T> toQueryWrapper() {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        this.applyToQueryWrapper(queryWrapper);
        return queryWrapper;
    }

    public void applyToQueryWrapper(QueryWrapper<T> queryWrapper) {
        if (isGeneral()) {
            applyGeneral(queryWrapper);
        } else if (isLike()) {
            applyLike(queryWrapper);
        } else if (isNested()) {
            applyNested(queryWrapper);
        } else {
            throw new IllegalArgumentException("Unsupported operator: " + operator);
        }
    }

    private boolean isGeneral() {
        return GeneralOperator.contains(operator);
    }

    private boolean isLike() {
        return LikeOperator.contains(operator);
    }

    private boolean isNested() {
        return "and".equals(operator) || "or".equals(operator) || "not".equals(operator);
    }

    private void applyGeneral(QueryWrapper<T> queryWrapper) {
        checkColumn();

        SqlKeyword sqlKeyword = GeneralOperator.get(operator).getSqlKeyword();
        try {
            generalMethod.invoke(queryWrapper, true, column, sqlKeyword, value);
        } catch (InvocationTargetException | IllegalAccessException e) {
            // TODO: Handle reflection exceptions
            throw new RuntimeException(e);
        }
    }

    private void applyLike(QueryWrapper<T> queryWrapper) {
        checkColumn();

        LikeOperator likeOperator = LikeOperator.of(operator);
        try {
            likeMethod.invoke(queryWrapper, true, likeOperator.getSqlKeyword(), column, value, likeOperator.getSqlLike());
        } catch (InvocationTargetException | IllegalAccessException e) {
            // TODO: Handle reflection exceptions
            throw new RuntimeException(e);
        }
    }

    private void applyNested(QueryWrapper<T> queryWrapper) {
        if (conditions == null || conditions.isEmpty()) {
            throw new IllegalArgumentException("Invalid conditions: " + conditions);
        }

        if ("and".equals(operator)) {
            conditions.forEach(condition -> queryWrapper.and(condition::applyToQueryWrapper));
        } else if ("or".equals(operator)) {
            conditions.forEach(condition -> queryWrapper.or(condition::applyToQueryWrapper));
        } else if ("not".equals(operator)) {
            conditions.forEach(condition -> queryWrapper.not(condition::applyToQueryWrapper));
        } else {
            throw new RuntimeException("This should never be thrown!");
        }
    }

    private void checkColumn() {
        if (column == null) {
            throw new IllegalArgumentException("Invalid column: column cannot be null");
        }

        // TODO: implement check column existing in entity class with cache
    }
}