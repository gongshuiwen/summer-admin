package io.summernova.admin.core.protocal.query;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author gongshuiwen
 */
class TestCompositeCondition {

    @Test
    void testGetSql() {
        String sql = "( " +
                "( name = 'mock1' OR name = 'mock2' ) " +
                "AND ( create_time > '2020-01-01 00:00:00' AND create_time < '2024-01-01 00:00:00' ) " +
                "AND NOT ( create_user = 1 OR create_user = 2 ) " +
                "AND NOT ( id = 1 AND create_user = 3 ) " +
                "AND NOT is_deleted = true )";

        SimpleCondition simpleCondition1 = SimpleCondition.of("name", "=", "mock1");
        SimpleCondition simpleCondition2 = SimpleCondition.of("name", "=", "mock2");
        CompositeCondition compositeCondition1 = CompositeCondition.or(List.of(simpleCondition1, simpleCondition2));

        SimpleCondition simpleCondition3 = SimpleCondition.of("create_time", ">", "2020-01-01 00:00:00");
        SimpleCondition simpleCondition4 = SimpleCondition.of("create_time", "<", "2024-01-01 00:00:00");
        CompositeCondition compositeCondition2 = CompositeCondition.and(List.of(simpleCondition3, simpleCondition4));

        SimpleCondition simpleCondition5 = SimpleCondition.of("create_user", "=", 1);
        SimpleCondition simpleCondition6 = SimpleCondition.of("create_user", "=", 2);
        CompositeCondition compositeCondition3 = CompositeCondition.notOr(List.of(simpleCondition5, simpleCondition6));

        SimpleCondition simpleCondition7 = SimpleCondition.of("id", "=", 1);
        SimpleCondition simpleCondition8 = SimpleCondition.of("create_user", "=", 3);
        CompositeCondition compositeCondition4 = CompositeCondition.notAnd(List.of(simpleCondition7, simpleCondition8));

        SimpleCondition simpleCondition9 = SimpleCondition.of("is_deleted", "=", true);
        CompositeCondition compositeCondition5 = CompositeCondition.not(simpleCondition9);

        CompositeCondition compositeCondition6 = CompositeCondition.and(List.of(compositeCondition1, compositeCondition2, compositeCondition3, compositeCondition4, compositeCondition5));

        assertEquals(sql, compositeCondition6.getSql());
    }
}