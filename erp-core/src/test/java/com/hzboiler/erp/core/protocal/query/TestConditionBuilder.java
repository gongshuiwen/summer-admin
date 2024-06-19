package com.hzboiler.erp.core.protocal.query;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author gongshuiwen
 */
class TestConditionBuilder {

    @Test
    void testBuild() {
        String sql = "( ( name = 'mock1' OR name = 'mock2' ) AND ( createTime > '2020-01-01 00:00:00' AND createTime < '2024-01-01 00:00:00' ) AND NOT ( createUser = 1 OR createUser = 2 ) AND id != 1 )";
        Condition condition = ConditionBuilder.
                and(
                        ConditionBuilder.or(
                                ConditionBuilder.simple("name", "=", "mock1"),
                                ConditionBuilder.simple("name", "=", "mock2")
                        ),
                        ConditionBuilder.and(
                                ConditionBuilder.simple("createTime", ">", "2020-01-01 00:00:00"),
                                ConditionBuilder.simple("createTime", "<", "2024-01-01 00:00:00")
                        ),
                        ConditionBuilder.notOr(
                                ConditionBuilder.simple("createUser", "=", 1),
                                ConditionBuilder.simple("createUser", "=", 2)
                        ),
                        ConditionBuilder.simple("id", "!=", 1)
                ).build();

        assertEquals(sql, condition.getSql());
    }
}
