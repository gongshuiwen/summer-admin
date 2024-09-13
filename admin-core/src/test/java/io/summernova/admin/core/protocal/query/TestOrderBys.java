package io.summernova.admin.core.protocal.query;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author gongshuiwen
 */
class TestOrderBys {

    @Test
    void testStaticMethodOf1() {
        OrderBys orderBys = OrderBys.of(OrderBy.asc("name"), OrderBy.desc("id"));
        assertEquals("name ASC,id DESC", orderBys.toString());
    }

    @Test
    void testStaticMethodOf2() {
        OrderBys orderBys = OrderBys.of("_id");
        assertEquals("id DESC", orderBys.toString());
    }

    @Test
    void testStaticMethodOf3() {
        OrderBys orderBys = OrderBys.of("name,_id");
        assertEquals("name ASC,id DESC", orderBys.toString());
    }

    @Test
    void testStaticMethodOf4() {
        OrderBys orderBys = OrderBys.of("name", "_id");
        assertEquals("name ASC,id DESC", orderBys.toString());
    }
}
