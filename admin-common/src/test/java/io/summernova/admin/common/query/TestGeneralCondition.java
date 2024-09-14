package io.summernova.admin.common.query;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestGeneralCondition {

    @Test
    void testOf() {
        GeneralCondition condition = GeneralCondition.of("name", GeneralOperator.EQ, "abc");
        assertEquals("name", condition.getField());
        assertEquals("=", condition.getOperator());
        assertEquals("abc", condition.getValue());
    }

    @Test
    void testEq() {
        GeneralCondition condition = GeneralCondition.eq("name", "abc");
        assertEquals("name", condition.getField());
        assertEquals("=", condition.getOperator());
        assertEquals("abc", condition.getValue());
    }

    @Test
    void testNe() {
        GeneralCondition condition = GeneralCondition.ne("name", "abc");
        assertEquals("name", condition.getField());
        assertEquals("!=", condition.getOperator());
        assertEquals("abc", condition.getValue());
    }

    @Test
    void testLt() {
        GeneralCondition condition = GeneralCondition.lt("name", "abc");
        assertEquals("name", condition.getField());
        assertEquals("<", condition.getOperator());
        assertEquals("abc", condition.getValue());
    }

    @Test
    void testGt() {
        GeneralCondition condition = GeneralCondition.gt("name", "abc");
        assertEquals("name", condition.getField());
        assertEquals(">", condition.getOperator());
        assertEquals("abc", condition.getValue());
    }

    @Test
    void testLe() {
        GeneralCondition condition = GeneralCondition.le("name", "abc");
        assertEquals("name", condition.getField());
        assertEquals("<=", condition.getOperator());
        assertEquals("abc", condition.getValue());
    }

    @Test
    void testGe() {
        GeneralCondition condition = GeneralCondition.ge("name", "abc");
        assertEquals("name", condition.getField());
        assertEquals(">=", condition.getOperator());
        assertEquals("abc", condition.getValue());
    }
}
