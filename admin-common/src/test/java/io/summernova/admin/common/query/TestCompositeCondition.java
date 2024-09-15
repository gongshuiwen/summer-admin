package io.summernova.admin.common.query;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author gongshuiwen
 */
class TestCompositeCondition {

    @Test
    void testOfAnd() {
        CompositeCondition condition = CompositeCondition.of(
                CompositeOperator.AND,
                    CompareCondition.gt("create_time", "2020-01-01 00:00:00"),
                    CompareCondition.le("create_time", "2024-01-01 00:00:00"));
        assertEquals("( create_time > '2020-01-01 00:00:00' AND create_time <= '2024-01-01 00:00:00' )", condition.toString());
    }

    @Test
    void testOfOr() {
        CompositeCondition condition = CompositeCondition.of(
            CompositeOperator.OR,
                CompareCondition.eq("name", "mock1"),
                CompareCondition.eq("name", "mock2")
        );
        assertEquals("( name = 'mock1' OR name = 'mock2' )", condition.toString());
    }

    @Test
    void testOfNot() {
        CompositeCondition condition = CompositeCondition.of(
                CompositeOperator.NOT,
                CompareCondition.eq("name", "mock1")
        );
        assertEquals("NOT name = 'mock1'", condition.toString());
    }

    @Test
    void testAnd() {
        CompositeCondition condition = CompositeCondition.and(
                CompareCondition.gt("create_time", "2020-01-01 00:00:00"),
                CompareCondition.le("create_time", "2024-01-01 00:00:00"));
        assertEquals("( create_time > '2020-01-01 00:00:00' AND create_time <= '2024-01-01 00:00:00' )", condition.toString());
    }

    @Test
    void testOr() {
        CompositeCondition condition = CompositeCondition.or(
                CompareCondition.eq("name", "mock1"),
                CompareCondition.eq("name", "mock2"));
        assertEquals("( name = 'mock1' OR name = 'mock2' )", condition.toString());
    }

    @Test
    void testNot() {
        CompositeCondition condition = CompositeCondition.not(CompareCondition.eq("name", "mock1"));
        assertEquals("NOT name = 'mock1'", condition.toString());
    }

    @Test
    void testNotAnd() {
        CompositeCondition condition = CompositeCondition.notAnd(
                CompareCondition.gt("create_time", "2020-01-01 00:00:00"),
                CompareCondition.le("create_time", "2024-01-01 00:00:00"));
        assertEquals("NOT ( create_time > '2020-01-01 00:00:00' AND create_time <= '2024-01-01 00:00:00' )", condition.toString());
    }

    @Test
    void testNotOr() {
        CompositeCondition condition = CompositeCondition.notOr(
                CompareCondition.eq("name", "mock1"),
                CompareCondition.eq("name", "mock2"));
        assertEquals("NOT ( name = 'mock1' OR name = 'mock2' )", condition.toString());
    }

    @Test
    void testComposite() {
        String sql = "( " +
                "( name = 'mock1' OR name = 'mock2' ) " +
                "AND ( create_time > '2020-01-01 00:00:00' AND create_time < '2024-01-01 00:00:00' ) " +
                "AND NOT ( create_user = 1 OR create_user = 2 ) " +
                "AND NOT ( id = 1 AND create_user = 3 ) " +
                "AND NOT is_deleted = true )";

        CompareCondition compareCondition1 = CompareCondition.eq("name", "mock1");
        CompareCondition compareCondition2 = CompareCondition.eq("name", "mock2");
        CompositeCondition compositeCondition1 = CompositeCondition.or(compareCondition1, compareCondition2);

        CompareCondition compareCondition3 = CompareCondition.gt("create_time", "2020-01-01 00:00:00");
        CompareCondition compareCondition4 = CompareCondition.lt("create_time", "2024-01-01 00:00:00");
        CompositeCondition compositeCondition2 = CompositeCondition.and(compareCondition3, compareCondition4);

        CompareCondition compareCondition5 = CompareCondition.eq("create_user", 1);
        CompareCondition compareCondition6 = CompareCondition.eq("create_user", 2);
        CompositeCondition compositeCondition3 = CompositeCondition.notOr(compareCondition5, compareCondition6);

        CompareCondition compareCondition7 = CompareCondition.eq("id", 1);
        CompareCondition compareCondition8 = CompareCondition.eq("create_user", 3);
        CompositeCondition compositeCondition4 = CompositeCondition.notAnd(compareCondition7, compareCondition8);

        CompareCondition compareCondition9 = CompareCondition.eq("is_deleted", true);
        CompositeCondition compositeCondition5 = CompositeCondition.not(compareCondition9);

        CompositeCondition compositeCondition6 = CompositeCondition.and(compositeCondition1, compositeCondition2, compositeCondition3, compositeCondition4, compositeCondition5);

        assertEquals(sql, compositeCondition6.toString());
    }
}