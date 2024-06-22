package com.hzboiler.erp.core.protocal.query;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hzboiler.erp.core.model.BaseModel;
import lombok.Getter;

/**
 * @author gongshuiwen
 */
@Getter
public class OrderBys {

    private final OrderBy[] orderBys;

    // prevent external instantiation
    private OrderBys(OrderBy[] orderBys) {
        this.orderBys = orderBys;
    }

    public static OrderBys of(OrderBy... orderBys) {
        if (orderBys == null || orderBys.length == 0)
            throw new IllegalArgumentException("The orderBys cannot be null or empty.");
        return new OrderBys(orderBys);
    }

    public void applyToQueryWrapper(QueryWrapper<? extends BaseModel> queryWrapper) {
        for (OrderBy orderBy : orderBys)
            orderBy.applyToQueryWrapper(queryWrapper);
    }
}
