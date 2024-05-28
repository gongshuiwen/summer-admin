package com.hzboiler.core.protocal;

import com.baomidou.mybatisplus.core.enums.SqlKeyword;
import com.baomidou.mybatisplus.core.enums.SqlLike;
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

    LIKE("like", SqlKeyword.LIKE, SqlLike.DEFAULT),
    LIKE_LEFT("likeLeft", SqlKeyword.LIKE, SqlLike.LEFT),
    LIKE_RIGHT("likeRight", SqlKeyword.LIKE, SqlLike.RIGHT),
    NOT_LIKE("notLike", SqlKeyword.NOT_LIKE, SqlLike.DEFAULT),
    NOT_LIKE_LEFT("notLikeLeft", SqlKeyword.NOT_LIKE, SqlLike.LEFT),
    NOT_LIKE_RIGHT("notLikeRight", SqlKeyword.NOT_LIKE, SqlLike.RIGHT)
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
    private final SqlKeyword sqlKeyword;
    private final SqlLike sqlLike;
}
