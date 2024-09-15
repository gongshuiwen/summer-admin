package io.summernova.admin.core.query.adapter;

import io.summernova.admin.common.query.GeneralCondition;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestGeneralConditionQueryWrapperAdapter extends QueryWrapperAdapterTestBase<GeneralConditionQueryWrapperAdapter> {

    @Test
    void testApplyToQueryWrapperEq() {
        queryWrapperAdapter.applyToQueryWrapper(GeneralCondition.eq("id", 100), mockQueryWrapper);
        assertEquals("(id = #{ew.paramNameValuePairs.MPGENVAL1})", mockQueryWrapper.getSqlSegment());
        assertEquals(100, mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL1"));
    }

    @Test
    void testApplyToQueryWrapperNe() {
        queryWrapperAdapter.applyToQueryWrapper(GeneralCondition.ne("id", 100), mockQueryWrapper);
        assertEquals("(id <> #{ew.paramNameValuePairs.MPGENVAL1})", mockQueryWrapper.getSqlSegment());
        assertEquals(100, mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL1"));
    }

    @Test
    void testApplyToQueryWrapperLt() {
        queryWrapperAdapter.applyToQueryWrapper(GeneralCondition.lt("id", 100), mockQueryWrapper);
        assertEquals("(id < #{ew.paramNameValuePairs.MPGENVAL1})", mockQueryWrapper.getSqlSegment());
        assertEquals(100, mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL1"));
    }

    @Test
    void testApplyToQueryWrapperGt() {
        queryWrapperAdapter.applyToQueryWrapper(GeneralCondition.gt("id", 100), mockQueryWrapper);
        assertEquals("(id > #{ew.paramNameValuePairs.MPGENVAL1})", mockQueryWrapper.getSqlSegment());
        assertEquals(100, mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL1"));
    }

    @Test
    void testApplyToQueryWrapperLe() {
        queryWrapperAdapter.applyToQueryWrapper(GeneralCondition.le("id", 100), mockQueryWrapper);
        assertEquals("(id <= #{ew.paramNameValuePairs.MPGENVAL1})", mockQueryWrapper.getSqlSegment());
        assertEquals(100, mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL1"));
    }

    @Test
    void testApplyToQueryWrapperGe() {
        queryWrapperAdapter.applyToQueryWrapper(GeneralCondition.ge("id", 100), mockQueryWrapper);
        assertEquals("(id >= #{ew.paramNameValuePairs.MPGENVAL1})", mockQueryWrapper.getSqlSegment());
        assertEquals(100, mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL1"));
    }
}
