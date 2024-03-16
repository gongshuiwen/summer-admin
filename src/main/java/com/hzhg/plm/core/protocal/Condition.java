package com.hzhg.plm.core.protocal;

import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.enums.SqlKeyword;
import com.baomidou.mybatisplus.core.enums.SqlLike;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

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

    private static final Set<String> generalSet = new HashSet<>();
    static {
        generalSet.add("=");
        generalSet.add("!=");
        generalSet.add(">");
        generalSet.add(">=");
        generalSet.add("<");
        generalSet.add("<=");
    }

    private static final Map<String, SqlKeyword> sqlKeywordMap = new HashMap<>();
    static {
        sqlKeywordMap.put("=", SqlKeyword.EQ);
        sqlKeywordMap.put("!=", SqlKeyword.NE);
        sqlKeywordMap.put(">", SqlKeyword.GT);
        sqlKeywordMap.put(">=", SqlKeyword.GE);
        sqlKeywordMap.put("<", SqlKeyword.LT);
        sqlKeywordMap.put("<=", SqlKeyword.LE);
        sqlKeywordMap.put("like", SqlKeyword.LIKE);
        sqlKeywordMap.put("like left", SqlKeyword.LIKE);
        sqlKeywordMap.put("like right", SqlKeyword.LIKE);
        sqlKeywordMap.put("not like", SqlKeyword.NOT_LIKE);
        sqlKeywordMap.put("not like left", SqlKeyword.NOT_LIKE);
        sqlKeywordMap.put("not like right", SqlKeyword.NOT_LIKE);
    }

    private static final Map<String, SqlLike> sqlLikeMap = new HashMap<>();
    static {
        sqlLikeMap.put("like", SqlLike.DEFAULT);
        sqlLikeMap.put("like left", SqlLike.LEFT);
        sqlLikeMap.put("like right", SqlLike.RIGHT);
        sqlLikeMap.put("not like", SqlLike.DEFAULT);
        sqlLikeMap.put("not like left", SqlLike.LEFT);
        sqlLikeMap.put("not like right", SqlLike.RIGHT);
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
        return generalSet.contains(operator);
    }

    private boolean isLike() {
        return sqlLikeMap.containsKey(operator);
    }

    private boolean isNested() {
        return "and".equals(operator) || "or".equals(operator) || "not".equals(operator);
    }

    private void applyGeneral(QueryWrapper<T> queryWrapper) {
        checkColumn();
        checkValue();

        try {
            generalMethod.invoke(queryWrapper, true, column, getSqlKeyword(), value);
        } catch (InvocationTargetException | IllegalAccessException e) {
            // TODO: Handle reflection exceptions
            throw new RuntimeException(e);
        }
    }

    private void applyLike(QueryWrapper<T> queryWrapper) {
        checkColumn();
        checkValue();

        try {
            likeMethod.invoke(queryWrapper, true, getSqlKeyword(), column, value, getSqlLike());
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

    private void checkValue() {
        if (value == null) {
            throw new IllegalArgumentException("Invalid value: value cannot be null");
        }
    }

    private SqlKeyword getSqlKeyword() {
        if (operator == null) {
            throw new IllegalArgumentException();
        }
        return sqlKeywordMap.get(operator);
    }

    private SqlLike getSqlLike() {
        if (operator == null) {
            throw new IllegalArgumentException();
        }
        return sqlLikeMap.get(operator);
    }
}