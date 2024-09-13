package io.summernova.admin.core.protocal.query.adapter;

import io.summernova.admin.common.query.NullCondition;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestNullConditionQueryWrapperAdapter extends QueryWrapperAdapterTestBase<NullConditionQueryWrapperAdapter> {

    @Test
    void testApplyToQueryWrapperIsNull() {
        queryWrapperAdapter.applyToQueryWrapper(NullCondition.isNull("name"), mockQueryWrapper);
        assertEquals("(name IS NULL)", mockQueryWrapper.getSqlSegment());
    }

    @Test
    void testApplyToQueryWrapperIsNotNull() {
        queryWrapperAdapter.applyToQueryWrapper(NullCondition.isNotNull("name"), mockQueryWrapper);
        assertEquals("(name IS NOT NULL)", mockQueryWrapper.getSqlSegment());
    }
}
