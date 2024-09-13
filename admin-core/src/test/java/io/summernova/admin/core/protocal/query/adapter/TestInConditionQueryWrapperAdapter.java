package io.summernova.admin.core.protocal.query.adapter;

import io.summernova.admin.common.query.InCondition;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestInConditionQueryWrapperAdapter extends QueryWrapperAdapterTestBase<InConditionQueryWrapperAdapter> {

    @Test
    void testApplyToQueryWrapperIn() {
        queryWrapperAdapter.applyToQueryWrapper(InCondition.in("name", List.of("a", "b")), mockQueryWrapper);
        assertEquals("(name IN (#{ew.paramNameValuePairs.MPGENVAL1},#{ew.paramNameValuePairs.MPGENVAL2}))", mockQueryWrapper.getSqlSegment());
        assertEquals("a", mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL1"));
        assertEquals("b", mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL2"));
    }

    @Test
    void testApplyToQueryWrapperNotIn() {
        queryWrapperAdapter.applyToQueryWrapper(InCondition.notIn("name", List.of("a", "b")), mockQueryWrapper);
        assertEquals("(name NOT IN (#{ew.paramNameValuePairs.MPGENVAL1},#{ew.paramNameValuePairs.MPGENVAL2}))", mockQueryWrapper.getSqlSegment());
        assertEquals("a", mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL1"));
        assertEquals("b", mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL2"));
    }
}
