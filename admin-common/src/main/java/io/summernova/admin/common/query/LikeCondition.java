package io.summernova.admin.common.query;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author gongshuiwen
 */
@Slf4j
@Getter
public final class LikeCondition extends Condition {

    private final String field;
    private final String value;

    // prevent external instantiation
    private LikeCondition(String field, LikeOperator likeOperator, String value) {
        super(likeOperator.getName());
        this.field = field;
        this.value = value;
    }

    // -------------------------
    // Static factory methods
    // -------------------------
    public static LikeCondition of(String field, LikeOperator likeOperator, String value) {
        checkField(field);
        return new LikeCondition(field, likeOperator, value);
    }

    public static LikeCondition of(String field, String likeOperator, String value) {
        return new LikeCondition(field, LikeOperator.of(likeOperator), value);
    }

    public static LikeCondition like(String field, String value) {
        checkField(field);
        return new LikeCondition(field, LikeOperator.LIKE, value);
    }

    public static LikeCondition likeLeft(String field, String value) {
        checkField(field);
        return new LikeCondition(field, LikeOperator.LIKE_LEFT, value);
    }

    public static LikeCondition likeRight(String field, String value) {
        checkField(field);
        return new LikeCondition(field, LikeOperator.LIKE_RIGHT, value);
    }

    public static LikeCondition notLike(String field, String value) {
        checkField(field);
        return new LikeCondition(field, LikeOperator.NOT_LIKE, value);
    }

    public static LikeCondition notLikeLeft(String field, String value) {
        checkField(field);
        return new LikeCondition(field, LikeOperator.NOT_LIKE_LEFT, value);
    }

    public static LikeCondition notLikeRight(String field, String value) {
        checkField(field);
        return new LikeCondition(field, LikeOperator.NOT_LIKE_RIGHT, value);
    }

    @Override
    public String getSql() {
        // TODO: SQL injection protection
        log.warn("!!! This is an experimental feature, which can lead to SQL injection risks, " +
                "so please use it with a clear understanding of how to avoid that risk.");
        return field + " " + operator.toUpperCase() + " '" + value + "'";
    }

    @Override
    public String toString() {
        return field + " " + operator.toUpperCase() + " '" + value + "'";
    }
}