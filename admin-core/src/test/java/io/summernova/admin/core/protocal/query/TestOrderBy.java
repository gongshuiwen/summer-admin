package io.summernova.admin.core.protocal.query;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author gongshuiwen
 */
class TestOrderBy {

    @Test
    void testAsc() {
        assertEquals("name ASC", OrderBy.asc("name").toString());
    }

    @Test
    void testDesc() {
        assertEquals("name DESC", OrderBy.desc("name").toString());
    }
}
