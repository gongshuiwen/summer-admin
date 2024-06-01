package com.hzboiler.erp.core.protocal.query;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hzboiler.erp.core.model.BaseModel;
import lombok.Getter;

import java.util.List;
import java.util.Objects;

/**
 * @author gongshuiwen
 */
@Getter
public class CompositeCondition extends Condition {

    public enum Operator {
        AND,
        OR,
        NOT,
    }

    // sub conditions
    private final List<Condition> conditions;

    // prevent external instantiation
    private CompositeCondition(Operator operator, List<Condition> conditions) {
        super(operator.name().toLowerCase());
        this.conditions = conditions;
    }

    public static CompositeCondition and(List<Condition> conditions) {
        checkConditions(conditions);
        return new CompositeCondition(Operator.AND, conditions);
    }

    public static CompositeCondition or(List<Condition> conditions) {
        checkConditions(conditions);
        return new CompositeCondition(Operator.OR, conditions);
    }

    public static CompositeCondition not(List<Condition> conditions) {
        checkConditions(conditions);
        return new CompositeCondition(Operator.NOT, conditions);
    }

    public static CompositeCondition of(Operator operator, List<Condition> conditions) {
        checkConditions(conditions);
        return new CompositeCondition(operator, conditions);
    }

    private static void checkConditions(List<Condition> conditions) {
        if (conditions == null || conditions.isEmpty())
            throw new IllegalArgumentException("conditions must not be null or empty.");

        // check items
        conditions.forEach((condition) -> Objects.requireNonNull(condition, "The item of conditions must not be null."));
    }

    @Override
    public void applyToQueryWrapper(QueryWrapper<? extends BaseModel> queryWrapper) {
        if (conditions == null || conditions.isEmpty()) {
            throw new IllegalArgumentException("conditions can not be null or empty");
        }

        if ("and".equals(getOperator())) {
            conditions.forEach(condition -> queryWrapper.and(condition::applyToQueryWrapper));
        } else if ("or".equals(getOperator())) {
            conditions.forEach(condition -> queryWrapper.or(condition::applyToQueryWrapper));
        } else if ("not".equals(getOperator())) {
            conditions.forEach(condition -> queryWrapper.not(condition::applyToQueryWrapper));
        } else {
            throw new RuntimeException("This should never be thrown!");
        }
    }

    @Override
    public <T extends BaseModel> QueryWrapper<T> toQueryWrapper(Class<T> clazz) {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        this.applyToQueryWrapper(queryWrapper);
        return queryWrapper;
    }
}