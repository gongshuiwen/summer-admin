package io.summernova.admin.core.dal.query.adapter;

import io.summernova.admin.common.query.OrderBy;
import io.summernova.admin.common.query.OrderBys;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author gongshuiwen
 */
class TestOrderBysQueryWrapperAdapter extends QueryWrapperAdapterTestBase<OrderBysQueryWrapperAdapter> {

    @Test
    void testApplyOrderBysToQueryWrapper() {
        OrderBysQueryWrapperAdapter.applyOrderBysToQueryWrapper(
                OrderBys.of(OrderBy.asc("name"), OrderBy.desc("id")), mockQueryWrapper);
        assertEquals(" ORDER BY name ASC,id DESC", mockQueryWrapper.getSqlSegment());
    }

    @Test
    void testApplyToQueryWrapper() {
        queryWrapperAdapter.applyToQueryWrapper(
                OrderBys.of(OrderBy.asc("name"), OrderBy.desc("id")), mockQueryWrapper);
        assertEquals(" ORDER BY name ASC,id DESC", mockQueryWrapper.getSqlSegment());
    }
}

