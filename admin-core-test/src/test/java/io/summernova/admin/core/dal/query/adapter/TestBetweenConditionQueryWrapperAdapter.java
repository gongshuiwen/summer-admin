package io.summernova.admin.core.dal.query.adapter;

import io.summernova.admin.common.query.BetweenCondition;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author gongshuiwen
 */
class TestBetweenConditionQueryWrapperAdapter extends QueryWrapperAdapterTestBase<BetweenConditionQueryWrapperAdapter> {

    @Test
    void testApplyToQueryWrapperBetween() {
        queryWrapperAdapter.applyToQueryWrapper(BetweenCondition.between("age", 20, 35), mockQueryWrapper);
        assertEquals("(age BETWEEN #{ew.paramNameValuePairs.MPGENVAL1} AND #{ew.paramNameValuePairs.MPGENVAL2})", mockQueryWrapper.getSqlSegment());
        assertEquals(20, mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL1"));
        assertEquals(35, mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL2"));
    }

    @Test
    void testApplyToQueryWrapperNotBetween() {
        queryWrapperAdapter.applyToQueryWrapper(BetweenCondition.notBetween("age", 20, 35), mockQueryWrapper);
        assertEquals("(age NOT BETWEEN #{ew.paramNameValuePairs.MPGENVAL1} AND #{ew.paramNameValuePairs.MPGENVAL2})", mockQueryWrapper.getSqlSegment());
        assertEquals(20, mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL1"));
        assertEquals(35, mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL2"));
    }
}
