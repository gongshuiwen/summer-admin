package io.summernova.admin.common.query;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestCompareCondition {

    @Test
    void testOf() {
        CompareCondition condition = CompareCondition.of("name", CompareOperator.EQ, "abc");
        assertEquals("name", condition.getField());
        assertEquals("=", condition.getOperator());
        assertEquals("abc", condition.getValue());
    }

    @Test
    void testEq() {
        CompareCondition condition = CompareCondition.eq("name", "abc");
        assertEquals("name", condition.getField());
        assertEquals("=", condition.getOperator());
        assertEquals("abc", condition.getValue());
    }

    @Test
    void testNe() {
        CompareCondition condition = CompareCondition.ne("name", "abc");
        assertEquals("name", condition.getField());
        assertEquals("!=", condition.getOperator());
        assertEquals("abc", condition.getValue());
    }

    @Test
    void testLt() {
        CompareCondition condition = CompareCondition.lt("name", "abc");
        assertEquals("name", condition.getField());
        assertEquals("<", condition.getOperator());
        assertEquals("abc", condition.getValue());
    }

    @Test
    void testGt() {
        CompareCondition condition = CompareCondition.gt("name", "abc");
        assertEquals("name", condition.getField());
        assertEquals(">", condition.getOperator());
        assertEquals("abc", condition.getValue());
    }

    @Test
    void testLe() {
        CompareCondition condition = CompareCondition.le("name", "abc");
        assertEquals("name", condition.getField());
        assertEquals("<=", condition.getOperator());
        assertEquals("abc", condition.getValue());
    }

    @Test
    void testGe() {
        CompareCondition condition = CompareCondition.ge("name", "abc");
        assertEquals("name", condition.getField());
        assertEquals(">=", condition.getOperator());
        assertEquals("abc", condition.getValue());
    }
}
