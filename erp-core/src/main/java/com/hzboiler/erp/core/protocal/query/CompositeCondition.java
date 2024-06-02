package com.hzboiler.erp.core.protocal.query;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hzboiler.erp.core.model.BaseModel;
import lombok.Getter;

import java.util.List;
import java.util.Objects;

/**
 * CompositeCondition is used to combine multiple conditions.
 *
 * @author gongshuiwen
 */
@Getter
public class CompositeCondition extends Condition {

    // string constants for composite operator
    public static final String OPERATOR_AND = "and";
    public static final String OPERATOR_OR = "or";
    public static final String OPERATOR_NOT = "not";

    // sub conditions
    private final List<Condition> conditions;

    // prevent external instantiation
    private CompositeCondition(String operator, List<Condition> conditions) {
        super(operator);
        this.conditions = conditions;
    }

    public static CompositeCondition and(List<Condition> conditions) {
        checkConditions(conditions);
        return new CompositeCondition(OPERATOR_AND, conditions);
    }

    public static CompositeCondition or(List<Condition> conditions) {
        checkConditions(conditions);
        return new CompositeCondition(OPERATOR_OR, conditions);
    }

    public static CompositeCondition not(Condition condition) {
        Objects.requireNonNull(condition, "condition of 'not' operator must not be null.");
        return new CompositeCondition(OPERATOR_NOT, List.of(condition));
    }

    public static CompositeCondition notAnd(List<Condition> conditions) {
        return not(and(conditions));
    }

    public static CompositeCondition notOr(List<Condition> conditions) {
        return not(or(conditions));
    }

    public static CompositeCondition of(String operator, List<Condition> conditions) {
        if (OPERATOR_NOT.equals(operator)) {
            if (conditions == null) throw new IllegalArgumentException("conditions must not be null.");
            if (conditions.size() != 1)  throw new IllegalArgumentException("conditions of 'not' operator must be only one.");
            return CompositeCondition.not(conditions.get(0));
        } else if (OPERATOR_AND.equals(operator) || OPERATOR_OR.equals(operator)) {
            checkConditions(conditions);
            return new CompositeCondition(operator, conditions);
        } else {
            throw new IllegalArgumentException("Unsupported operator '" + operator + "' for CompositeCondition.");
        }
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

        if (OPERATOR_AND.equals(getOperator())) {
            conditions.forEach(condition -> queryWrapper.and(condition::applyToQueryWrapper));
        } else if (OPERATOR_OR.equals(getOperator())) {
            conditions.forEach(condition -> queryWrapper.or(condition::applyToQueryWrapper));
        } else if (OPERATOR_NOT.equals(getOperator())) {
            conditions.forEach(condition -> queryWrapper.not(condition::applyToQueryWrapper));
        } else {
            throw new IllegalArgumentException("Unsupported operator '" + getOperator() + "' for CompositeCondition.");
        }
    }

    @Override
    public <T extends BaseModel> QueryWrapper<T> toQueryWrapper(Class<T> clazz) {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        this.applyToQueryWrapper(queryWrapper);
        return queryWrapper;
    }
}