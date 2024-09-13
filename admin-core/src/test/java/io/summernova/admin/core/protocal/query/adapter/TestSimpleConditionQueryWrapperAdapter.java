package io.summernova.admin.core.protocal.query.adapter;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.summernova.admin.core.protocal.query.SimpleCondition;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestSimpleConditionQueryWrapperAdapter extends QueryWrapperAdapterTestBase<SimpleConditionQueryWrapperAdapter> {

    @Test
    void testApplyToQueryWrapperGeneral() {
        SimpleCondition condition;

        mockQueryWrapper = new QueryWrapper<>();
        condition = SimpleCondition.of("id", "=", 100);
        queryWrapperAdapter.applyToQueryWrapper(condition, mockQueryWrapper);
        assertEquals("(id = #{ew.paramNameValuePairs.MPGENVAL1})", mockQueryWrapper.getSqlSegment());
        assertEquals(100, mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL1"));

        mockQueryWrapper = new QueryWrapper<>();
        condition = SimpleCondition.of("id", "<", 100);
        queryWrapperAdapter.applyToQueryWrapper(condition, mockQueryWrapper);
        assertEquals("(id < #{ew.paramNameValuePairs.MPGENVAL1})", mockQueryWrapper.getSqlSegment());
        assertEquals(100, mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL1"));

        mockQueryWrapper = new QueryWrapper<>();
        condition = SimpleCondition.of("id", ">", 100);
        queryWrapperAdapter.applyToQueryWrapper(condition, mockQueryWrapper);
        assertEquals("(id > #{ew.paramNameValuePairs.MPGENVAL1})", mockQueryWrapper.getSqlSegment());
        assertEquals(100, mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL1"));

        mockQueryWrapper = new QueryWrapper<>();
        condition = SimpleCondition.of("id", "!=", 100);
        queryWrapperAdapter.applyToQueryWrapper(condition, mockQueryWrapper);
        assertEquals("(id <> #{ew.paramNameValuePairs.MPGENVAL1})", mockQueryWrapper.getSqlSegment());
        assertEquals(100, mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL1"));

        mockQueryWrapper = new QueryWrapper<>();
        condition = SimpleCondition.of("id", "<=", 100);
        queryWrapperAdapter.applyToQueryWrapper(condition, mockQueryWrapper);
        assertEquals("(id <= #{ew.paramNameValuePairs.MPGENVAL1})", mockQueryWrapper.getSqlSegment());
        assertEquals(100, mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL1"));

        mockQueryWrapper = new QueryWrapper<>();
        condition = SimpleCondition.of("id", ">=", 100);
        queryWrapperAdapter.applyToQueryWrapper(condition, mockQueryWrapper);
        assertEquals("(id >= #{ew.paramNameValuePairs.MPGENVAL1})", mockQueryWrapper.getSqlSegment());
        assertEquals(100, mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL1"));
    }

    @Test
    void testApplyToQueryWrapperLike() {
        SimpleCondition condition;

        mockQueryWrapper = new QueryWrapper<>();
        condition = SimpleCondition.of("name", "like", "mock");
        queryWrapperAdapter.applyToQueryWrapper(condition, mockQueryWrapper);
        assertEquals("(name LIKE #{ew.paramNameValuePairs.MPGENVAL1})", mockQueryWrapper.getSqlSegment());
        assertEquals("%mock%", mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL1"));

        mockQueryWrapper = new QueryWrapper<>();
        condition = SimpleCondition.of("name", "likeLeft", "mock");
        queryWrapperAdapter.applyToQueryWrapper(condition, mockQueryWrapper);
        assertEquals("(name LIKE #{ew.paramNameValuePairs.MPGENVAL1})", mockQueryWrapper.getSqlSegment());
        assertEquals("%mock", mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL1"));

        mockQueryWrapper = new QueryWrapper<>();
        condition = SimpleCondition.of("name", "likeRight", "mock");
        queryWrapperAdapter.applyToQueryWrapper(condition, mockQueryWrapper);
        assertEquals("(name LIKE #{ew.paramNameValuePairs.MPGENVAL1})", mockQueryWrapper.getSqlSegment());
        assertEquals("mock%", mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL1"));

        mockQueryWrapper = new QueryWrapper<>();
        condition = SimpleCondition.of("name", "notLike", "mock");
        queryWrapperAdapter.applyToQueryWrapper(condition, mockQueryWrapper);
        assertEquals("(name NOT LIKE #{ew.paramNameValuePairs.MPGENVAL1})", mockQueryWrapper.getSqlSegment());
        assertEquals("%mock%", mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL1"));

        mockQueryWrapper = new QueryWrapper<>();
        condition = SimpleCondition.of("name", "notLikeLeft", "mock");
        queryWrapperAdapter.applyToQueryWrapper(condition, mockQueryWrapper);
        assertEquals("(name NOT LIKE #{ew.paramNameValuePairs.MPGENVAL1})", mockQueryWrapper.getSqlSegment());
        assertEquals("%mock", mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL1"));

        mockQueryWrapper = new QueryWrapper<>();
        condition = SimpleCondition.of("name", "notLikeRight", "mock");
        queryWrapperAdapter.applyToQueryWrapper(condition, mockQueryWrapper);
        assertEquals("(name NOT LIKE #{ew.paramNameValuePairs.MPGENVAL1})", mockQueryWrapper.getSqlSegment());
        assertEquals("mock%", mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL1"));
    }

}
