package io.summernova.admin.core.protocal.query;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author gongshuiwen
 */
class TestConditionBuilder {

    @Test
    void testBuild() {
        String sql = "( ( name = 'mock1' OR name = 'mock2' ) AND ( create_time > '2020-01-01 00:00:00' AND create_time < '2024-01-01 00:00:00' ) AND NOT ( create_user = 1 OR create_user = 2 ) AND id != 1 )";
        Condition condition = ConditionBuilder.
                and(
                        ConditionBuilder.or(
                                ConditionBuilder.simple("name", "=", "mock1"),
                                ConditionBuilder.simple("name", "=", "mock2")
                        ),
                        ConditionBuilder.and(
                                ConditionBuilder.simple("create_time", ">", "2020-01-01 00:00:00"),
                                ConditionBuilder.simple("create_time", "<", "2024-01-01 00:00:00")
                        ),
                        ConditionBuilder.notOr(
                                ConditionBuilder.simple("create_user", "=", 1),
                                ConditionBuilder.simple("create_user", "=", 2)
                        ),
                        ConditionBuilder.simple("id", "!=", 1)
                ).build();

        assertEquals(sql, condition.getSql());
    }
}
