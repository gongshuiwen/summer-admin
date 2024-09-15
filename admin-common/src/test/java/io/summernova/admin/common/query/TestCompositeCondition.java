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
                    GeneralCondition.gt("create_time", "2020-01-01 00:00:00"),
                    GeneralCondition.le("create_time", "2024-01-01 00:00:00"));
        assertEquals("( create_time > '2020-01-01 00:00:00' AND create_time <= '2024-01-01 00:00:00' )", condition.toString());
    }

    @Test
    void testOfOr() {
        CompositeCondition condition = CompositeCondition.of(
            CompositeOperator.OR,
                GeneralCondition.eq("name", "mock1"),
                GeneralCondition.eq("name", "mock2")
        );
        assertEquals("( name = 'mock1' OR name = 'mock2' )", condition.toString());
    }

    @Test
    void testOfNot() {
        CompositeCondition condition = CompositeCondition.of(
                CompositeOperator.NOT,
                GeneralCondition.eq("name", "mock1")
        );
        assertEquals("NOT name = 'mock1'", condition.toString());
    }

    @Test
    void testAnd() {
        CompositeCondition condition = CompositeCondition.and(
                GeneralCondition.gt("create_time", "2020-01-01 00:00:00"),
                GeneralCondition.le("create_time", "2024-01-01 00:00:00"));
        assertEquals("( create_time > '2020-01-01 00:00:00' AND create_time <= '2024-01-01 00:00:00' )", condition.toString());
    }

    @Test
    void testOr() {
        CompositeCondition condition = CompositeCondition.or(
                GeneralCondition.eq("name", "mock1"),
                GeneralCondition.eq("name", "mock2"));
        assertEquals("( name = 'mock1' OR name = 'mock2' )", condition.toString());
    }

    @Test
    void testNot() {
        CompositeCondition condition = CompositeCondition.not(GeneralCondition.eq("name", "mock1"));
        assertEquals("NOT name = 'mock1'", condition.toString());
    }

    @Test
    void testNotAnd() {
        CompositeCondition condition = CompositeCondition.notAnd(
                GeneralCondition.gt("create_time", "2020-01-01 00:00:00"),
                GeneralCondition.le("create_time", "2024-01-01 00:00:00"));
        assertEquals("NOT ( create_time > '2020-01-01 00:00:00' AND create_time <= '2024-01-01 00:00:00' )", condition.toString());
    }

    @Test
    void testNotOr() {
        CompositeCondition condition = CompositeCondition.notOr(
                GeneralCondition.eq("name", "mock1"),
                GeneralCondition.eq("name", "mock2"));
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

        GeneralCondition generalCondition1 = GeneralCondition.eq("name", "mock1");
        GeneralCondition generalCondition2 = GeneralCondition.eq("name", "mock2");
        CompositeCondition compositeCondition1 = CompositeCondition.or(generalCondition1, generalCondition2);

        GeneralCondition generalCondition3 = GeneralCondition.gt("create_time", "2020-01-01 00:00:00");
        GeneralCondition generalCondition4 = GeneralCondition.lt("create_time", "2024-01-01 00:00:00");
        CompositeCondition compositeCondition2 = CompositeCondition.and(generalCondition3, generalCondition4);

        GeneralCondition generalCondition5 = GeneralCondition.eq("create_user", 1);
        GeneralCondition generalCondition6 = GeneralCondition.eq("create_user", 2);
        CompositeCondition compositeCondition3 = CompositeCondition.notOr(generalCondition5, generalCondition6);

        GeneralCondition generalCondition7 = GeneralCondition.eq("id", 1);
        GeneralCondition generalCondition8 = GeneralCondition.eq("create_user", 3);
        CompositeCondition compositeCondition4 = CompositeCondition.notAnd(generalCondition7, generalCondition8);

        GeneralCondition generalCondition9 = GeneralCondition.eq("is_deleted", true);
        CompositeCondition compositeCondition5 = CompositeCondition.not(generalCondition9);

        CompositeCondition compositeCondition6 = CompositeCondition.and(compositeCondition1, compositeCondition2, compositeCondition3, compositeCondition4, compositeCondition5);

        assertEquals(sql, compositeCondition6.toString());
    }
}