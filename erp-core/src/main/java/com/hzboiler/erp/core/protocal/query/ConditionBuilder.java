package com.hzboiler.erp.core.protocal.query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author gongshuiwen
 */
public final class ConditionBuilder {

    private final String operator;
    private final List<Condition> conditions = new ArrayList<>();

    // prevent external initialization
    private ConditionBuilder(String operator) {
        this.operator = operator;
    }

    public static ConditionBuilder and(ConditionBuilder... conditions) {
        ConditionBuilder builder = new ConditionBuilder(CompositeCondition.OPERATOR_AND);
        Arrays.stream(conditions).forEach(c -> builder.conditions.add(c.build()));
        return builder;
    }

    public static ConditionBuilder or(ConditionBuilder... conditions) {
        ConditionBuilder builder = new ConditionBuilder(CompositeCondition.OPERATOR_OR);
        Arrays.stream(conditions).forEach(c -> builder.conditions.add(c.build()));
        return builder;
    }

    public static ConditionBuilder not(ConditionBuilder condition) {
        ConditionBuilder builder = new ConditionBuilder(CompositeCondition.OPERATOR_NOT);
        builder.conditions.add(condition.build());
        return builder;
    }

    public static ConditionBuilder notAnd(ConditionBuilder... conditions) {
        return not(and(conditions));
    }

    public static ConditionBuilder notOr(ConditionBuilder... conditions) {
        return not(or(conditions));
    }

    public static ConditionBuilder simple(String field, String operator, Object value) {
        ConditionBuilder builder = new ConditionBuilder(operator);
        builder.conditions.add(SimpleCondition.of(field, operator, value));
        return builder;
    }

    public static ConditionBuilder eq(String field, Object value) {
        return simple(field, GeneralOperator.EQ.getName(), value);
    }

    public static ConditionBuilder ne(String field, Object value) {
        return simple(field, GeneralOperator.NE.getName(), value);
    }

    public static ConditionBuilder lt(String field, Object value) {
        return simple(field, GeneralOperator.LT.getName(), value);
    }

    public static ConditionBuilder le(String field, Object value) {
        return simple(field, GeneralOperator.LE.getName(), value);
    }

    public static ConditionBuilder gt(String field, Object value) {
        return simple(field, GeneralOperator.GT.getName(), value);
    }

    public static ConditionBuilder ge(String field, Object value) {
        return simple(field, GeneralOperator.GE.getName(), value);
    }

    public static ConditionBuilder like(String field, Object value) {
        return simple(field, LikeOperator.LIKE.getName(), value);
    }

    public static ConditionBuilder likeLeft(String field, Object value) {
        return simple(field, LikeOperator.LIKE_LEFT.getName(), value);
    }

    public static ConditionBuilder likeRight(String field, Object value) {
        return simple(field, LikeOperator.LIKE_RIGHT.getName(), value);
    }

    public static ConditionBuilder notLike(String field, Object value) {
        return simple(field, LikeOperator.NOT_LIKE.getName(), value);
    }

    public static ConditionBuilder notLikeLeft(String field, Object value) {
        return simple(field, LikeOperator.NOT_LIKE_LEFT.getName(), value);
    }

    public static ConditionBuilder notLikeRight(String field, Object value) {
        return simple(field, LikeOperator.NOT_LIKE_RIGHT.getName(), value);
    }

    public Condition build() {
        if (CompositeCondition.OPERATOR_NOT.equals(operator)) {
            return CompositeCondition.not(conditions.get(0));
        } else if (CompositeCondition.OPERATOR_AND.equals(operator)) {
            return CompositeCondition.and(conditions);
        } else if (CompositeCondition.OPERATOR_OR.equals(operator)) {
            return CompositeCondition.or(conditions);
        } else {
            return conditions.get(0);
        }
    }
}
