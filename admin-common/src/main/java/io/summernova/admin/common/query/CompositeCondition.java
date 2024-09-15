package io.summernova.admin.common.query;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * CompositeCondition is used to combine multiple conditions.
 *
 * @author gongshuiwen
 */
@Getter
public final class CompositeCondition extends Condition {

    public static final String DELIMITER_AND = " AND ";
    public static final String DELIMITER_OR = " OR ";
    public static final String DELIMITER_NOT = "NOT ";

    // sub conditions
    private final Condition[] conditions;

    // prevent external instantiation
    private CompositeCondition(CompositeOperator compositeOperator, Condition... conditions) {
        super(compositeOperator.getName());
        this.conditions = conditions;
    }

    // ------------------------
    // Static factory methods
    // ------------------------
    public static CompositeCondition of(CompositeOperator compositeOperator, Condition... conditions) {
        if (compositeOperator == CompositeOperator.NOT) {
            if (conditions == null)
                throw new IllegalArgumentException("conditions of 'not' operator must not be null.");
            if (conditions.length != 1)
                throw new IllegalArgumentException("conditions of 'not' operator must be only one.");
            if (conditions[0] == null)
                throw new IllegalArgumentException("condition of 'not' operator must not be null.");
            return new CompositeCondition(compositeOperator, conditions[0]);
        } else if (compositeOperator == CompositeOperator.AND || compositeOperator == CompositeOperator.OR) {
            checkConditions(conditions);
            return new CompositeCondition(compositeOperator, conditions);
        } else {
            throw new IllegalArgumentException("Unsupported composite operator: " + compositeOperator);
        }
    }

    public static CompositeCondition of(CompositeOperator compositeOperator, List<Condition> conditions) {
        return of(compositeOperator, conditions.toArray(new Condition[0]));
    }

    public static CompositeCondition of(String compositeOperator, Condition... conditions) {
        return of(CompositeOperator.of(compositeOperator), conditions);
    }

    public static CompositeCondition of(String compositeOperator, List<Condition> conditions) {
        return of(CompositeOperator.of(compositeOperator), conditions.toArray(new Condition[0]));
    }

    public static CompositeCondition and(Condition... conditions) {
        checkConditions(conditions);
        return new CompositeCondition(CompositeOperator.AND, conditions);
    }

    public static CompositeCondition and(List<Condition> conditions) {
        return and(conditions.toArray(new Condition[0]));
    }

    public static CompositeCondition or(Condition... conditions) {
        checkConditions(conditions);
        return new CompositeCondition(CompositeOperator.OR, conditions);
    }

    public static CompositeCondition or(List<Condition> conditions) {
        return or(conditions.toArray(new Condition[0]));
    }

    public static CompositeCondition not(Condition condition) {
        Objects.requireNonNull(condition, "condition of 'not' operator must not be null.");
        return new CompositeCondition(CompositeOperator.NOT, condition);
    }

    public static CompositeCondition notAnd(Condition... conditions) {
        return not(and(conditions));
    }

    public static CompositeCondition notAnd(List<Condition> conditions) {
        return not(and(conditions));
    }

    public static CompositeCondition notOr(Condition... conditions) {
        return not(or(conditions));
    }

    public static CompositeCondition notOr(List<Condition> conditions) {
        return not(or(conditions));
    }

    private static void checkConditions(Condition... conditions) {
        if (conditions == null || conditions.length == 0)
            throw new IllegalArgumentException("conditions must not be null or empty.");

        // check items
        for (Condition condition : conditions) {
            Objects.requireNonNull(condition, "The item of conditions must not be null.");
        }
    }

    @Override
    public String toString() {
        return getSql();
    }

    @Override
    public String getSql() {
        String delimiter = getDelimiter();
        if (operator.equals(CompositeOperator.NOT.getName()))
            return delimiter + conditions[0].toString();
        return Arrays.stream(conditions)
                .map(Condition::getSql)
                .collect(Collectors.joining(delimiter, "( ", " )"));
    }

    private String getDelimiter() {
        String delimiter;
        if (operator.equals(CompositeOperator.AND.getName())) {
            delimiter = DELIMITER_AND;
        } else if (operator.equals(CompositeOperator.OR.getName())) {
            delimiter = DELIMITER_OR;
        } else if (operator.equals(CompositeOperator.NOT.getName())) {
            delimiter = DELIMITER_NOT;
        } else {
            throw new IllegalArgumentException("Unsupported composite operator: " + operator);
        }
        return delimiter;
    }
}