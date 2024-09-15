package io.summernova.admin.common.query;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author gongshuiwen
 */
@Getter
@AllArgsConstructor
public enum CompareOperator {
    EQ("="),
    LT("<"),
    GT(">"),
    NE("!="),
    LE("<="),
    GE(">=");

    private static final Map<String, CompareOperator> LOOKUP = new HashMap<>();

    static {
        for (CompareOperator operator : CompareOperator.values())
            LOOKUP.put(operator.name, operator);
    }

    private final String name;

    public static CompareOperator of(String name) {
        CompareOperator operator = LOOKUP.get(name);
        if (operator == null)
            throw new IllegalArgumentException("No CompareOperator with name '" + name + "' found.");
        return operator;
    }

    public static boolean contains(String name) {
        return LOOKUP.containsKey(name);
    }
}
