package com.hzhg.plm.core.protocal;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hzhg.plm.core.entity.Mock;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class TestCondition {

    private static final LocalDateTime CREATE_TIME = LocalDateTime.of(2020, 1, 1, 0, 0, 0);

    private static final List<Condition<Mock>> QUERY_CONDITIONS = Arrays.asList(
            new Condition<>("name", "=", "mock"),
            new Condition<>("createTime", ">=", CREATE_TIME),
            new Condition<>("createUser", "=", 1)
    );

    @Test
    public void testApplyToQueryWrapperSimple() {
        QueryWrapper<Mock> mockQueryWrapper = new QueryWrapper<>();
        Condition<Mock> condition = new Condition<>("name", "=", "mock");
        condition.applyToQueryWrapper(mockQueryWrapper);
        Assertions.assertEquals("(name = #{ew.paramNameValuePairs.MPGENVAL1})", mockQueryWrapper.getSqlSegment());
        Assertions.assertEquals("mock", mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL1"));
    }

    @Test
    public void testApplyToWrapperNestedAnd() {
        QueryWrapper<Mock> mockQueryWrapper = new QueryWrapper<>();
        Condition<Mock> condition = new Condition<>("and", QUERY_CONDITIONS);
        condition.applyToQueryWrapper(mockQueryWrapper);
        Assertions.assertEquals("((name = #{ew.paramNameValuePairs.MPGENVAL1}) AND (createTime >= #{ew.paramNameValuePairs.MPGENVAL2}) AND (createUser = #{ew.paramNameValuePairs.MPGENVAL3}))", mockQueryWrapper.getSqlSegment());
        Assertions.assertEquals("mock", mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL1"));
        Assertions.assertEquals(CREATE_TIME, mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL2"));
        Assertions.assertEquals(1, mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL3"));
    }

    @Test
    public void testApplyToWrapperNestedOr() {
        QueryWrapper<Mock> mockQueryWrapper = new QueryWrapper<>();
        Condition<Mock> condition = new Condition<>("or", QUERY_CONDITIONS);
        condition.applyToQueryWrapper(mockQueryWrapper);
        Assertions.assertEquals("((name = #{ew.paramNameValuePairs.MPGENVAL1}) OR (createTime >= #{ew.paramNameValuePairs.MPGENVAL2}) OR (createUser = #{ew.paramNameValuePairs.MPGENVAL3}))", mockQueryWrapper.getSqlSegment());
        Assertions.assertEquals("mock", mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL1"));
        Assertions.assertEquals(CREATE_TIME, mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL2"));
        Assertions.assertEquals(1, mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL3"));
    }

    @Test
    public void testApplyToWrapperNestedNot() {
        QueryWrapper<Mock> mockQueryWrapper = new QueryWrapper<>();
        Condition<Mock> condition = new Condition<>("not", QUERY_CONDITIONS);
        condition.applyToQueryWrapper(mockQueryWrapper);
        Assertions.assertEquals("(NOT (name = #{ew.paramNameValuePairs.MPGENVAL1}) AND NOT (createTime >= #{ew.paramNameValuePairs.MPGENVAL2}) AND NOT (createUser = #{ew.paramNameValuePairs.MPGENVAL3}))", mockQueryWrapper.getSqlSegment());
        Assertions.assertEquals("mock", mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL1"));
        Assertions.assertEquals(CREATE_TIME, mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL2"));
        Assertions.assertEquals(1, mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL3"));
    }
}