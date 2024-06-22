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

    public static OrderBys of(String... orderByStrings) {
        OrderBy[] orderBys = new OrderBy[orderByStrings.length];
        for (int i = 0; i < orderByStrings.length; i++) {
            if (orderByStrings[i].startsWith("_")) {
                orderBys[i] = OrderBy.desc(orderByStrings[i].substring(1));
            } else {
                orderBys[i] = OrderBy.asc(orderByStrings[i]);
            }
        }
        return new OrderBys(orderBys);
    }

    public static OrderBys parse(String orderBysString) {
        if (orderBysString == null || orderBysString.isEmpty())
            throw new IllegalArgumentException("The orderBys cannot be null or empty.");
        String[] orderByStrings = orderBysString.split(",");
        return of(orderByStrings);
    }

    public void applyToQueryWrapper(QueryWrapper<? extends BaseModel> queryWrapper) {
        for (OrderBy orderBy : orderBys)
            orderBy.applyToQueryWrapper(queryWrapper);
    }
}
