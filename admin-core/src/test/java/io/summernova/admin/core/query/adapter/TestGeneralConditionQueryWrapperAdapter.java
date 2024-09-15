package io.summernova.admin.core.query.adapter;

import io.summernova.admin.common.query.CompareCondition;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestCompareConditionQueryWrapperAdapter extends QueryWrapperAdapterTestBase<CompareConditionQueryWrapperAdapter> {

    @Test
    void testApplyToQueryWrapperEq() {
        queryWrapperAdapter.applyToQueryWrapper(CompareCondition.eq("id", 100), mockQueryWrapper);
        assertEquals("(id = #{ew.paramNameValuePairs.MPGENVAL1})", mockQueryWrapper.getSqlSegment());
        assertEquals(100, mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL1"));
    }

    @Test
    void testApplyToQueryWrapperNe() {
        queryWrapperAdapter.applyToQueryWrapper(CompareCondition.ne("id", 100), mockQueryWrapper);
        assertEquals("(id <> #{ew.paramNameValuePairs.MPGENVAL1})", mockQueryWrapper.getSqlSegment());
        assertEquals(100, mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL1"));
    }

    @Test
    void testApplyToQueryWrapperLt() {
        queryWrapperAdapter.applyToQueryWrapper(CompareCondition.lt("id", 100), mockQueryWrapper);
        assertEquals("(id < #{ew.paramNameValuePairs.MPGENVAL1})", mockQueryWrapper.getSqlSegment());
        assertEquals(100, mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL1"));
    }

    @Test
    void testApplyToQueryWrapperGt() {
        queryWrapperAdapter.applyToQueryWrapper(CompareCondition.gt("id", 100), mockQueryWrapper);
        assertEquals("(id > #{ew.paramNameValuePairs.MPGENVAL1})", mockQueryWrapper.getSqlSegment());
        assertEquals(100, mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL1"));
    }

    @Test
    void testApplyToQueryWrapperLe() {
        queryWrapperAdapter.applyToQueryWrapper(CompareCondition.le("id", 100), mockQueryWrapper);
        assertEquals("(id <= #{ew.paramNameValuePairs.MPGENVAL1})", mockQueryWrapper.getSqlSegment());
        assertEquals(100, mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL1"));
    }

    @Test
    void testApplyToQueryWrapperGe() {
        queryWrapperAdapter.applyToQueryWrapper(CompareCondition.ge("id", 100), mockQueryWrapper);
        assertEquals("(id >= #{ew.paramNameValuePairs.MPGENVAL1})", mockQueryWrapper.getSqlSegment());
        assertEquals(100, mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL1"));
    }
}
