package io.summernova.admin.core.protocal.query.adapter;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.summernova.admin.common.query.OrderBy;

/**
 * @author gongshuiwen
 */
final class OrderByQueryWrapperAdapter implements QueryWrapperAdapter<OrderBy> {

    @Override
    public <M> void applyToQueryWrapper(OrderBy orderBy, QueryWrapper<M> queryWrapper) {
        OrderBy.OrderByType type = orderBy.getType();
        if (type == OrderBy.OrderByType.ASC) {
            queryWrapper.orderByAsc(orderBy.getField());
        } else if (type == OrderBy.OrderByType.DESC) {
            queryWrapper.orderByDesc(orderBy.getField());
        } else {
            throw new IllegalArgumentException("Invalid order by type: " + type);
        }
    }
}
