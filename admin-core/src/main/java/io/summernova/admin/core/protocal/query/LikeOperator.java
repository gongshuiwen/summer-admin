package io.summernova.admin.core.protocal.query;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author gongshuiwen
 */
@Getter
@AllArgsConstructor
public enum LikeOperator {

    LIKE("like"),
    LIKE_LEFT("likeLeft"),
    LIKE_RIGHT("likeRight"),
    NOT_LIKE("notLike"),
    NOT_LIKE_LEFT("notLikeLeft"),
    NOT_LIKE_RIGHT("notLikeRight")
    ;

    private static final Map<String, LikeOperator> lookup = new HashMap<>();
    static {
        for (LikeOperator operator : LikeOperator.values()) {
            lookup.put(operator.name, operator);
        }
    }

    public static LikeOperator of(String name) {
        LikeOperator operator = lookup.get(name);
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
