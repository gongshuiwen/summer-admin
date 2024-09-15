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
public enum CompositeOperator {
    AND("and"),
    OR("or"),
    NOT("not");

    private static final Map<String, CompositeOperator> LOOKUP = new HashMap<>();

    static {
        for (CompositeOperator operator : CompositeOperator.values())
            LOOKUP.put(operator.name, operator);
    }

    private final String name;

    public static CompositeOperator of(String name) {
        CompositeOperator operator = LOOKUP.get(name);
        if (operator == null)
            throw new IllegalArgumentException("No CompositeOperator with name '" + name + "' found.");
        return operator;
    }

    public static boolean contains(String name) {
        return LOOKUP.containsKey(name);
    }
}
