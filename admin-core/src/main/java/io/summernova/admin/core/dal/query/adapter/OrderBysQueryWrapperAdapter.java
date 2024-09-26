package io.summernova.admin.core.dal.query.adapter;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.summernova.admin.common.query.OrderBy;
import io.summernova.admin.common.query.OrderBys;

/**
 * @author gongshuiwen
 */
public final class OrderBysQueryWrapperAdapter implements QueryWrapperAdapter<OrderBys> {

    private static final OrderBysQueryWrapperAdapter INSTANCE = new OrderBysQueryWrapperAdapter();
    private static final OrderByQueryWrapperAdapter ORDER_BY_QUERY_WRAPPER_ADAPTER = new OrderByQueryWrapperAdapter();

    public static <M> void applyOrderBysToQueryWrapper(OrderBys orderBys, QueryWrapper<M> queryWrapper) {
        INSTANCE.applyToQueryWrapper(orderBys, queryWrapper);
    }

    @Override
    public <M> void applyToQueryWrapper(OrderBys orderBys, QueryWrapper<M> queryWrapper) {
        for (OrderBy orderBy : orderBys.getOrderBys()) {
            ORDER_BY_QUERY_WRAPPER_ADAPTER.applyToQueryWrapper(orderBy, queryWrapper);
        }
    }
}
