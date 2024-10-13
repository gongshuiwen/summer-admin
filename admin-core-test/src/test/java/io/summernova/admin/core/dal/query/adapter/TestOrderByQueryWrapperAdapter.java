package io.summernova.admin.core.dal.query.adapter;

import io.summernova.admin.common.query.OrderBy;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author gongshuiwen
 */
class TestOrderByQueryWrapperAdapter extends QueryWrapperAdapterTestBase<OrderByQueryWrapperAdapter> {

    @Test
    void testApplyToQueryWrapperAsc() {
        queryWrapperAdapter.applyToQueryWrapper(OrderBy.asc("name"), mockQueryWrapper);
        assertEquals(" ORDER BY name ASC", mockQueryWrapper.getSqlSegment());
    }

    @Test
    void testApplyToQueryWrapperDesc() {
        queryWrapperAdapter.applyToQueryWrapper(OrderBy.desc("name"), mockQueryWrapper);
        assertEquals(" ORDER BY name DESC", mockQueryWrapper.getSqlSegment());
    }
}
