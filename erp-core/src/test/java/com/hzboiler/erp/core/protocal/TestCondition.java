package com.hzboiler.erp.core.protocal;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hzboiler.erp.core.protocal.Condition;
import com.hzboiler.erp.core.model.Mock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

class TestCondition {

    static final LocalDateTime CREATE_TIME = LocalDateTime.of(2020, 1, 1, 0, 0, 0);

    static final List<Condition<Mock>> SIMPLE_QUERY_CONDITIONS = Arrays.asList(
            new Condition<>("name", "=", "mock"),
            new Condition<>("createTime", ">=", CREATE_TIME),
            new Condition<>("createUser", "=", 1)
    );

    @Test
    void testApplyToQueryWrapperGeneral() {
        QueryWrapper<Mock> mockQueryWrapper;
        Condition<Mock> condition;

        mockQueryWrapper = new QueryWrapper<>();
        condition = new Condition<>("id", "=", 100);
        condition.applyToQueryWrapper(mockQueryWrapper);
        Assertions.assertEquals("(id = #{ew.paramNameValuePairs.MPGENVAL1})", mockQueryWrapper.getSqlSegment());
        Assertions.assertEquals(100, mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL1"));

        mockQueryWrapper = new QueryWrapper<>();
        condition = new Condition<>("id", "<", 100);
        condition.applyToQueryWrapper(mockQueryWrapper);
        Assertions.assertEquals("(id < #{ew.paramNameValuePairs.MPGENVAL1})", mockQueryWrapper.getSqlSegment());
        Assertions.assertEquals(100, mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL1"));

        mockQueryWrapper = new QueryWrapper<>();
        condition = new Condition<>("id", ">", 100);
        condition.applyToQueryWrapper(mockQueryWrapper);
        Assertions.assertEquals("(id > #{ew.paramNameValuePairs.MPGENVAL1})", mockQueryWrapper.getSqlSegment());
        Assertions.assertEquals(100, mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL1"));

        mockQueryWrapper = new QueryWrapper<>();
        condition = new Condition<>("id", "!=", 100);
        condition.applyToQueryWrapper(mockQueryWrapper);
        Assertions.assertEquals("(id <> #{ew.paramNameValuePairs.MPGENVAL1})", mockQueryWrapper.getSqlSegment());
        Assertions.assertEquals(100, mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL1"));

        mockQueryWrapper = new QueryWrapper<>();
        condition = new Condition<>("id", "<=", 100);
        condition.applyToQueryWrapper(mockQueryWrapper);
        Assertions.assertEquals("(id <= #{ew.paramNameValuePairs.MPGENVAL1})", mockQueryWrapper.getSqlSegment());
        Assertions.assertEquals(100, mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL1"));

        mockQueryWrapper = new QueryWrapper<>();
        condition = new Condition<>("id", ">=", 100);
        condition.applyToQueryWrapper(mockQueryWrapper);
        Assertions.assertEquals("(id >= #{ew.paramNameValuePairs.MPGENVAL1})", mockQueryWrapper.getSqlSegment());
        Assertions.assertEquals(100, mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL1"));
    }

    @Test
    void testApplyToQueryWrapperLike() {
        QueryWrapper<Mock> mockQueryWrapper;
        Condition<Mock> condition;

        mockQueryWrapper = new QueryWrapper<>();
        condition = new Condition<>("name", "like", "mock");
        condition.applyToQueryWrapper(mockQueryWrapper);
        Assertions.assertEquals("(name LIKE #{ew.paramNameValuePairs.MPGENVAL1})", mockQueryWrapper.getSqlSegment());
        Assertions.assertEquals("%mock%", mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL1"));

        mockQueryWrapper = new QueryWrapper<>();
        condition = new Condition<>("name", "likeLeft", "mock");
        condition.applyToQueryWrapper(mockQueryWrapper);
        Assertions.assertEquals("(name LIKE #{ew.paramNameValuePairs.MPGENVAL1})", mockQueryWrapper.getSqlSegment());
        Assertions.assertEquals("%mock", mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL1"));

        mockQueryWrapper = new QueryWrapper<>();
        condition = new Condition<>("name", "likeRight", "mock");
        condition.applyToQueryWrapper(mockQueryWrapper);
        Assertions.assertEquals("(name LIKE #{ew.paramNameValuePairs.MPGENVAL1})", mockQueryWrapper.getSqlSegment());
        Assertions.assertEquals("mock%", mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL1"));

        mockQueryWrapper = new QueryWrapper<>();
        condition = new Condition<>("name", "notLike", "mock");
        condition.applyToQueryWrapper(mockQueryWrapper);
        Assertions.assertEquals("(name NOT LIKE #{ew.paramNameValuePairs.MPGENVAL1})", mockQueryWrapper.getSqlSegment());
        Assertions.assertEquals("%mock%", mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL1"));

        mockQueryWrapper = new QueryWrapper<>();
        condition = new Condition<>("name", "notLikeLeft", "mock");
        condition.applyToQueryWrapper(mockQueryWrapper);
        Assertions.assertEquals("(name NOT LIKE #{ew.paramNameValuePairs.MPGENVAL1})", mockQueryWrapper.getSqlSegment());
        Assertions.assertEquals("%mock", mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL1"));

        mockQueryWrapper = new QueryWrapper<>();
        condition = new Condition<>("name", "notLikeRight", "mock");
        condition.applyToQueryWrapper(mockQueryWrapper);
        Assertions.assertEquals("(name NOT LIKE #{ew.paramNameValuePairs.MPGENVAL1})", mockQueryWrapper.getSqlSegment());
        Assertions.assertEquals("mock%", mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL1"));
    }

    @Test
    void testApplyToQueryWrapperNestedSimpleAnd() {
        QueryWrapper<Mock> mockQueryWrapper = new QueryWrapper<>();
        Condition<Mock> condition = new Condition<>("and", SIMPLE_QUERY_CONDITIONS);
        condition.applyToQueryWrapper(mockQueryWrapper);
        Assertions.assertEquals("((name = #{ew.paramNameValuePairs.MPGENVAL1}) AND (createTime >= #{ew.paramNameValuePairs.MPGENVAL2}) AND (createUser = #{ew.paramNameValuePairs.MPGENVAL3}))", mockQueryWrapper.getSqlSegment());
        Assertions.assertEquals("mock", mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL1"));
        Assertions.assertEquals(CREATE_TIME, mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL2"));
        Assertions.assertEquals(1, mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL3"));
    }

    @Test
    void testApplyToQueryWrapperNestedSimpleOr() {
        QueryWrapper<Mock> mockQueryWrapper = new QueryWrapper<>();
        Condition<Mock> condition = new Condition<>("or", SIMPLE_QUERY_CONDITIONS);
        condition.applyToQueryWrapper(mockQueryWrapper);
        Assertions.assertEquals("((name = #{ew.paramNameValuePairs.MPGENVAL1}) OR (createTime >= #{ew.paramNameValuePairs.MPGENVAL2}) OR (createUser = #{ew.paramNameValuePairs.MPGENVAL3}))", mockQueryWrapper.getSqlSegment());
        Assertions.assertEquals("mock", mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL1"));
        Assertions.assertEquals(CREATE_TIME, mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL2"));
        Assertions.assertEquals(1, mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL3"));
    }

    @Test
    void testApplyToQueryWrapperNestedSimpleNot() {
        QueryWrapper<Mock> mockQueryWrapper = new QueryWrapper<>();
        Condition<Mock> condition = new Condition<>("not", SIMPLE_QUERY_CONDITIONS);
        condition.applyToQueryWrapper(mockQueryWrapper);
        Assertions.assertEquals("(NOT (name = #{ew.paramNameValuePairs.MPGENVAL1}) AND NOT (createTime >= #{ew.paramNameValuePairs.MPGENVAL2}) AND NOT (createUser = #{ew.paramNameValuePairs.MPGENVAL3}))", mockQueryWrapper.getSqlSegment());
        Assertions.assertEquals("mock", mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL1"));
        Assertions.assertEquals(CREATE_TIME, mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL2"));
        Assertions.assertEquals(1, mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL3"));
    }
}