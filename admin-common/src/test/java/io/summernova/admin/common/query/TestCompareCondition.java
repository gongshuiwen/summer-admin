package io.summernova.admin.common.query;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author gongshuiwen
 */
class TestCompareCondition {

    @Test
    void testOf() {
        CompareCondition condition = CompareCondition.of("age", CompareOperator.EQ, 18);
        assertEquals("age", condition.getField());
        assertEquals("=", condition.getOperator());
        assertEquals(18, condition.getValue());
    }

    @Test
    void testEq() {
        CompareCondition condition = CompareCondition.eq("age", 18);
        assertEquals("age", condition.getField());
        assertEquals("=", condition.getOperator());
        assertEquals(18, condition.getValue());
    }

    @Test
    void testNe() {
        CompareCondition condition = CompareCondition.ne("age", 18);
        assertEquals("age", condition.getField());
        assertEquals("!=", condition.getOperator());
        assertEquals(18, condition.getValue());
    }

    @Test
    void testLt() {
        CompareCondition condition = CompareCondition.lt("age", 18);
        assertEquals("age", condition.getField());
        assertEquals("<", condition.getOperator());
        assertEquals(18, condition.getValue());
    }

    @Test
    void testGt() {
        CompareCondition condition = CompareCondition.gt("age", 18);
        assertEquals("age", condition.getField());
        assertEquals(">", condition.getOperator());
        assertEquals(18, condition.getValue());
    }

    @Test
    void testLe() {
        CompareCondition condition = CompareCondition.le("age", 18);
        assertEquals("age", condition.getField());
        assertEquals("<=", condition.getOperator());
        assertEquals(18, condition.getValue());
    }

    @Test
    void testGe() {
        CompareCondition condition = CompareCondition.ge("age", 18);
        assertEquals("age", condition.getField());
        assertEquals(">=", condition.getOperator());
        assertEquals(18, condition.getValue());
    }
}
