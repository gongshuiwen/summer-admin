package com.hzboiler.erp.core.protocal.query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.hzboiler.erp.core.protocal.query.CompositeCondition.*;

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
        ConditionBuilder builder = new ConditionBuilder(OPERATOR_AND);
        Arrays.stream(conditions).forEach(c -> builder.conditions.add(c.build()));
        return builder;
    }

    public static ConditionBuilder or(ConditionBuilder... conditions) {
        ConditionBuilder builder = new ConditionBuilder(OPERATOR_OR);
        Arrays.stream(conditions).forEach(c -> builder.conditions.add(c.build()));
        return builder;
    }

    public static ConditionBuilder not(ConditionBuilder condition) {
        ConditionBuilder builder = new ConditionBuilder(OPERATOR_NOT);
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

    public Condition build() {
        if (OPERATOR_NOT.equals(operator)) {
            return CompositeCondition.not(conditions.get(0));
        } else if (OPERATOR_AND.equals(operator)) {
            return CompositeCondition.and(conditions);
        } else if (OPERATOR_OR.equals(operator)) {
            return CompositeCondition.or(conditions);
        } else {
            return conditions.get(0);
        }
    }
}
