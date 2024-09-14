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
public enum LikeOperator {
    LIKE("like"),
    LIKE_LEFT("likeLeft"),
    LIKE_RIGHT("likeRight"),
    NOT_LIKE("notLike"),
    NOT_LIKE_LEFT("notLikeLeft"),
    NOT_LIKE_RIGHT("notLikeRight");

    private static final Map<String, LikeOperator> LOOKUP = new HashMap<>();

    static {
        for (LikeOperator operator : LikeOperator.values())
            LOOKUP.put(operator.name, operator);
    }

    private final String name;

    public static LikeOperator of(String name) {
        LikeOperator operator = LOOKUP.get(name);
        if (operator == null)
            throw new IllegalArgumentException("No LikeOperator with name '" + name + "' found.");
        return operator;
    }

    public static boolean contains(String name) {
        return LOOKUP.containsKey(name);
    }
}
