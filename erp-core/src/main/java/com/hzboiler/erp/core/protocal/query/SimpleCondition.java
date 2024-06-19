package com.hzboiler.erp.core.protocal.query;

import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.enums.SqlKeyword;
import com.baomidou.mybatisplus.core.enums.SqlLike;
import com.hzboiler.erp.core.model.BaseModel;
import lombok.Getter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @author gongshuiwen
 */
@Getter
public class SimpleCondition extends Condition {

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

    private final String field;
    private final Object value;

    // prevent external instantiation
    private SimpleCondition(String field, String operator, Object value) {
        super(operator);
        this.field = field;
        this.value = value;
    }

    public static SimpleCondition of(String field, GeneralOperator operator, Object value) {
        checkField(field);
        checkValue(value);
        return new SimpleCondition(field, operator.getName(), value);
    }

    public static SimpleCondition of(String field, LikeOperator operator, Object value) {
        checkField(field);
        checkValue(value);
        return new SimpleCondition(field, operator.toString(), value);
    }

    public static SimpleCondition of(String field, String operator, Object value) {
        checkField(field);
        checkOperator(operator);
        checkValue(value);
        return new SimpleCondition(field, operator, value);
    }

    private static void checkField(String field) {
        Objects.requireNonNull(field, "field must not be null");
        if (field.isBlank()) {
            throw new IllegalArgumentException("field must not be blank");
        }
    }

    private static void checkOperator(String operator) {
        Objects.requireNonNull(operator, "operator must not be null");
        if (operator.isBlank()) {
            throw new IllegalArgumentException("operator must not be blank");
        }
        if (!GeneralOperator.contains(operator) && !LikeOperator.contains(operator)) {
            throw new IllegalArgumentException("Unsupported operator: " + operator);
        }
    }

    private static void checkValue(Object value) {
        // TODO: check value valid depends on field type and operator
    }

    @Override
    public void applyToQueryWrapper(QueryWrapper<? extends BaseModel> queryWrapper) {
        if (isGeneral()) {
            applyGeneral(queryWrapper);
        } else if (isLike()) {
            applyLike(queryWrapper);
        } else {
            throw new IllegalArgumentException("Unsupported operator '" + getOperator() + "' for SimpleCondition.");
        }
    }

    @Override
    public String getSql() {
        // TODO: SQL injection protection
        if (value instanceof String) {
            return field + " " + getOperator().toUpperCase() + " '" + value + "'";
        }
        return field + " " + getOperator().toUpperCase() + " " + value;
    }

    private boolean isGeneral() {
        return GeneralOperator.contains(getOperator());
    }

    private boolean isLike() {
        return LikeOperator.contains(getOperator());
    }

    private void applyGeneral(QueryWrapper<?> queryWrapper) {
        SqlKeyword sqlKeyword = GeneralOperator.get(getOperator()).getSqlKeyword();
        try {
            methodAddCondition.invoke(queryWrapper, true, field, sqlKeyword, value);
        } catch (InvocationTargetException | IllegalAccessException e) {
            // TODO: Handle reflection exceptions
            throw new RuntimeException(e);
        }
    }

    private void applyLike(QueryWrapper<?> queryWrapper) {
        LikeOperator likeOperator = LikeOperator.of(getOperator());
        try {
            methodLikeValue.invoke(queryWrapper, true, likeOperator.getSqlKeyword(), field, value, likeOperator.getSqlLike());
        } catch (InvocationTargetException | IllegalAccessException e) {
            // TODO: Handle reflection exceptions
            throw new RuntimeException(e);
        }
    }
}