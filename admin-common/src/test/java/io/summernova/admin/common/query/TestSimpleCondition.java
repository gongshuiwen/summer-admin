package io.summernova.admin.common.query;

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