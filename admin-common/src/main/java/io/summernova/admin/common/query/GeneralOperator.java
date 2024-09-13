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
    GE(">="),
    ;

    private static final Map<String, GeneralOperator> lookup = new HashMap<>();
    static {
        for (GeneralOperator operator : GeneralOperator.values()) {
            lookup.put(operator.name, operator);
        }
    }

    public static GeneralOperator get(String name) {
        GeneralOperator operator = lookup.get(name);
        if (operator == null) {
            throw new IllegalArgumentException("No operator named " + name);
        }
        return operator;
    }

    public static boolean contains(String name) {
        return lookup.containsKey(name);
    }

    private final String name;
}
