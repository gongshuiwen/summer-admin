package com.hzhg.plm.core.protocal;

import com.baomidou.mybatisplus.core.enums.SqlKeyword;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
public enum GeneralOperator {
    EQ("=", SqlKeyword.EQ),
    LT("<", SqlKeyword.LT),
    GT(">", SqlKeyword.GT),
    NE("!=", SqlKeyword.NE),
    LE("<=", SqlKeyword.LE),
    GE(">=", SqlKeyword.GE),
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
    private final SqlKeyword sqlKeyword;
}
