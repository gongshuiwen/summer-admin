package io.summernova.admin.core.protocal.query;

import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.enums.SqlKeyword;
import com.baomidou.mybatisplus.core.enums.SqlLike;
import io.summernova.admin.core.model.BaseModel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @author gongshuiwen
 */
@Slf4j
@Getter
public final class SimpleCondition extends Condition {

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
            throw new IllegalArgumentException("Unsupported operator '" + operator + "' for SimpleCondition.");
        }
    }

    @Override
    public String getSql() {
        // TODO: SQL injection protection
        log.warn("!!! This is an experimental feature, which can lead to SQL injection risks, " +
                "so please use it with a clear understanding of how to avoid that risk.");
        if (value instanceof String) {
            return field + " " + operator.toUpperCase() + " '" + value + "'";
        }
        return field + " " + operator.toUpperCase() + " " + value;
    }

    private boolean isGeneral() {
        return GeneralOperator.contains(operator);
    }

    private boolean isLike() {
        return LikeOperator.contains(operator);
    }

    private void applyGeneral(QueryWrapper<?> queryWrapper) {
        SqlKeyword sqlKeyword = GeneralOperator.get(operator).getSqlKeyword();
        try {
            methodAddCondition.invoke(queryWrapper, true, field, sqlKeyword, value);
        } catch (InvocationTargetException | IllegalAccessException e) {
            // TODO: Handle reflection exceptions
            throw new RuntimeException(e);
        }
    }

    private void applyLike(QueryWrapper<?> queryWrapper) {
        LikeOperator likeOperator = LikeOperator.of(operator);
        try {
            methodLikeValue.invoke(queryWrapper, true, likeOperator.getSqlKeyword(), field, value, likeOperator.getSqlLike());
        } catch (InvocationTargetException | IllegalAccessException e) {
            // TODO: Handle reflection exceptions
            throw new RuntimeException(e);
        }
    }
}