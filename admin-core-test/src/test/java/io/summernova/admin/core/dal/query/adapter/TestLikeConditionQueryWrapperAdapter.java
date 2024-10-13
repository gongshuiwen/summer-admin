package io.summernova.admin.core.dal.query.adapter;

import io.summernova.admin.common.query.LikeCondition;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author gongshuiwen
 */
class TestLikeConditionQueryWrapperAdapter extends QueryWrapperAdapterTestBase<LikeConditionQueryWrapperAdapter> {

    @Test
    void testApplyToQueryWrapperLike() {
        queryWrapperAdapter.applyToQueryWrapper(LikeCondition.like("name", "mock"), mockQueryWrapper);
        assertEquals("(name LIKE #{ew.paramNameValuePairs.MPGENVAL1})", mockQueryWrapper.getSqlSegment());
        assertEquals("%mock%", mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL1"));
    }

    @Test
    void testApplyToQueryWrapperLikeLeft() {
        queryWrapperAdapter.applyToQueryWrapper(LikeCondition.likeLeft("name", "mock"), mockQueryWrapper);
        assertEquals("(name LIKE #{ew.paramNameValuePairs.MPGENVAL1})", mockQueryWrapper.getSqlSegment());
        assertEquals("%mock", mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL1"));
    }

    @Test
    void testApplyToQueryWrapperLikeRight() {
        queryWrapperAdapter.applyToQueryWrapper(LikeCondition.likeRight("name", "mock"), mockQueryWrapper);
        assertEquals("(name LIKE #{ew.paramNameValuePairs.MPGENVAL1})", mockQueryWrapper.getSqlSegment());
        assertEquals("mock%", mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL1"));
    }

    @Test
    void testApplyToQueryWrapperNotLike() {
        queryWrapperAdapter.applyToQueryWrapper(LikeCondition.notLike("name", "mock"), mockQueryWrapper);
        assertEquals("(name NOT LIKE #{ew.paramNameValuePairs.MPGENVAL1})", mockQueryWrapper.getSqlSegment());
        assertEquals("%mock%", mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL1"));
    }

    @Test
    void testApplyToQueryWrapperNotLikeLeft() {
        queryWrapperAdapter.applyToQueryWrapper(LikeCondition.notLikeLeft("name", "mock"), mockQueryWrapper);
        assertEquals("(name NOT LIKE #{ew.paramNameValuePairs.MPGENVAL1})", mockQueryWrapper.getSqlSegment());
        assertEquals("%mock", mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL1"));
    }

    @Test
    void testApplyToQueryWrapperNotLikeRight() {
        queryWrapperAdapter.applyToQueryWrapper(LikeCondition.notLikeRight("name", "mock"), mockQueryWrapper);
        assertEquals("(name NOT LIKE #{ew.paramNameValuePairs.MPGENVAL1})", mockQueryWrapper.getSqlSegment());
        assertEquals("mock%", mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL1"));
    }
}
