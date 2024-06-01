package com.hzboiler.erp.core.protocal.query;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hzboiler.erp.core.model.Mock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

class TestCompositeCondition {

    static final LocalDateTime CREATE_TIME = LocalDateTime.of(2020, 1, 1, 0, 0, 0);

    static final List<Condition> SIMPLE_QUERY_CONDITIONS = Arrays.asList(
            SimpleCondition.of("name", "=", "mock"),
            SimpleCondition.of("createTime", ">=", CREATE_TIME),
            SimpleCondition.of("createUser", "=", 1)
    );

    @Test
    void testApplyToQueryWrapperCompositeAnd() {
        QueryWrapper<Mock> mockQueryWrapper = new QueryWrapper<>();
        Condition condition = CompositeCondition.and(SIMPLE_QUERY_CONDITIONS);
        condition.applyToQueryWrapper(mockQueryWrapper);
        Assertions.assertEquals("(" +
                "(name = #{ew.paramNameValuePairs.MPGENVAL1}) " +
                "AND " +
                "(createTime >= #{ew.paramNameValuePairs.MPGENVAL2}) " +
                "AND " +
                "(createUser = #{ew.paramNameValuePairs.MPGENVAL3}))", mockQueryWrapper.getSqlSegment());
        Assertions.assertEquals("mock", mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL1"));
        Assertions.assertEquals(CREATE_TIME, mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL2"));
        Assertions.assertEquals(1, mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL3"));
    }

    @Test
    void testApplyToQueryWrapperCompositeOr() {
        QueryWrapper<Mock> mockQueryWrapper = new QueryWrapper<>();
        Condition condition = CompositeCondition.or(SIMPLE_QUERY_CONDITIONS);
        condition.applyToQueryWrapper(mockQueryWrapper);
        Assertions.assertEquals("(" +
                "(name = #{ew.paramNameValuePairs.MPGENVAL1}) " +
                "OR " +
                "(createTime >= #{ew.paramNameValuePairs.MPGENVAL2}) " +
                "OR " +
                "(createUser = #{ew.paramNameValuePairs.MPGENVAL3}))"
                , mockQueryWrapper.getSqlSegment());
        Assertions.assertEquals("mock", mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL1"));
        Assertions.assertEquals(CREATE_TIME, mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL2"));
        Assertions.assertEquals(1, mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL3"));
    }

    @Test
    void testApplyToQueryWrapperCompositeNot() {
        QueryWrapper<Mock> mockQueryWrapper = new QueryWrapper<>();
        Condition condition = CompositeCondition.not(SIMPLE_QUERY_CONDITIONS);
        condition.applyToQueryWrapper(mockQueryWrapper);
        Assertions.assertEquals("(" +
                "NOT (name = #{ew.paramNameValuePairs.MPGENVAL1}) " +
                "AND " +
                "NOT (createTime >= #{ew.paramNameValuePairs.MPGENVAL2}) " +
                "AND " +
                "NOT (createUser = #{ew.paramNameValuePairs.MPGENVAL3}))", mockQueryWrapper.getSqlSegment());
        Assertions.assertEquals("mock", mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL1"));
        Assertions.assertEquals(CREATE_TIME, mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL2"));
        Assertions.assertEquals(1, mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL3"));
    }

    @Test
    void testApplyToQueryWrapperCompositeNested() {
        CompositeCondition subCondition1 = CompositeCondition.or(List.of(
                SimpleCondition.of("name", "=", "mock1"),
                SimpleCondition.of("name", "=", "mock2")
        ));

        CompositeCondition subCondition2 = CompositeCondition.and(List.of(
                SimpleCondition.of("createTime", ">", LocalDateTime.of(2020, 1, 1, 0, 0, 0)),
                SimpleCondition.of("createTime", "<", LocalDateTime.of(2022, 1, 1, 0, 0, 0))
        ));

        SimpleCondition subCondition3 = SimpleCondition.of("createUser", "!=", 1);

        Condition condition = CompositeCondition.and(List.of(subCondition1, subCondition2, subCondition3));

        QueryWrapper<Mock> mockQueryWrapper = new QueryWrapper<>();
        condition.applyToQueryWrapper(mockQueryWrapper);
        Assertions.assertEquals("(" +
                "(" +
                    "(name = #{ew.paramNameValuePairs.MPGENVAL1}) " +
                    "OR " +
                    "(name = #{ew.paramNameValuePairs.MPGENVAL2})" +
                ") " +
                "AND " +
                "(" +
                    "(createTime > #{ew.paramNameValuePairs.MPGENVAL3}) " +
                    "AND " +
                    "(createTime < #{ew.paramNameValuePairs.MPGENVAL4})" +
                ") " +
                "AND " +
                "(createUser <> #{ew.paramNameValuePairs.MPGENVAL5}))", mockQueryWrapper.getSqlSegment());
        Assertions.assertEquals("mock1", mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL1"));
        Assertions.assertEquals("mock2", mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL2"));
    }
}