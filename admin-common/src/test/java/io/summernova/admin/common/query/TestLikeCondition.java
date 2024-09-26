package io.summernova.admin.common.query;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author gongshuiwen
 */
class TestLikeCondition {

    @Test
    void testOf() {
        LikeCondition condition = LikeCondition.of("name", LikeOperator.LIKE, "abc");
        assertEquals("name", condition.getField());
        assertEquals("like", condition.getOperator());
        assertEquals("abc", condition.getValue());
    }

    @Test
    void testLike() {
        LikeCondition condition = LikeCondition.like("name", "abc");
        assertEquals("name", condition.getField());
        assertEquals("like", condition.getOperator());
        assertEquals("abc", condition.getValue());
    }

    @Test
    void testLikeLeft() {
        LikeCondition condition = LikeCondition.likeLeft("name", "abc");
        assertEquals("name", condition.getField());
        assertEquals("likeLeft", condition.getOperator());
        assertEquals("abc", condition.getValue());
    }

    @Test
    void testLikeRight() {
        LikeCondition condition = LikeCondition.likeRight("name", "abc");
        assertEquals("name", condition.getField());
        assertEquals("likeRight", condition.getOperator());
        assertEquals("abc", condition.getValue());
    }

    @Test
    void testNotLike() {
        LikeCondition condition = LikeCondition.notLike("name", "abc");
        assertEquals("name", condition.getField());
        assertEquals("notLike", condition.getOperator());
        assertEquals("abc", condition.getValue());
    }

    @Test
    void testNotLikeLeft() {
        LikeCondition condition = LikeCondition.notLikeLeft("name", "abc");
        assertEquals("name", condition.getField());
        assertEquals("notLikeLeft", condition.getOperator());
        assertEquals("abc", condition.getValue());
    }

    @Test
    void testNotLikeRight() {
        LikeCondition condition = LikeCondition.notLikeRight("name", "abc");
        assertEquals("name", condition.getField());
        assertEquals("notLikeRight", condition.getOperator());
        assertEquals("abc", condition.getValue());
    }
}
