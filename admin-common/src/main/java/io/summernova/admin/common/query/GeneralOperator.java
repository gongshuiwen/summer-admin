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
public enum GeneralOperator {
    EQ("="),
    LT("<"),
    GT(">"),
    NE("!="),
    LE("<="),
    GE(">=");

    private static final Map<String, GeneralOperator> LOOKUP = new HashMap<>();

    static {
        for (GeneralOperator operator : GeneralOperator.values())
            LOOKUP.put(operator.name, operator);
    }

    private final String name;

    public static GeneralOperator of(String name) {
        GeneralOperator operator = LOOKUP.get(name);
        if (operator == null)
            throw new IllegalArgumentException("No GeneralOperator with name '" + name + "' found.");
        return operator;
    }

    public static boolean contains(String name) {
        return LOOKUP.containsKey(name);
    }
}
