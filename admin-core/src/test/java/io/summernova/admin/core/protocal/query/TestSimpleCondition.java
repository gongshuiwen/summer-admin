package io.summernova.admin.core.protocal.query;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author gongshuiwen
 */
class TestSimpleCondition {

    @Test
    void testGetSql() {
        Condition condition = SimpleCondition.of("name", "like", "mock");
        assertEquals("name LIKE 'mock'", condition.getSql());
    }
}