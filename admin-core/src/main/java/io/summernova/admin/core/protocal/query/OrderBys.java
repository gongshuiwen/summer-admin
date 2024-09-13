package io.summernova.admin.core.protocal.query;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.summernova.admin.core.model.BaseModel;
import lombok.Getter;

/**
 * @author gongshuiwen
 */
@Getter
public class OrderBys implements QueryWrapperAdapter {

    public static final String SEPARATOR = ",";
    public static final String DESC_PREFIX = "_";

    private final OrderBy[] orderBys;

    // prevent external instantiation
    private OrderBys(OrderBy[] orderBys) {
        this.orderBys = orderBys;
    }

    public static OrderBys of(OrderBy... orderBys) {
        if (orderBys == null || orderBys.length == 0)
            throw new IllegalArgumentException("The orderBys must not be null or empty.");
        return new OrderBys(orderBys);
    }

    public static OrderBys of(String... orderByStrings) {
        OrderBy[] orderBys = new OrderBy[orderByStrings.length];
        for (int i = 0; i < orderByStrings.length; i++) {
            if (orderByStrings[i].startsWith(DESC_PREFIX)) {
                orderBys[i] = OrderBy.desc(orderByStrings[i].substring(1));
            } else {
                orderBys[i] = OrderBy.asc(orderByStrings[i]);
            }
        }
        return new OrderBys(orderBys);
    }

    public static OrderBys parse(String orderBysString) {
        if (orderBysString == null || orderBysString.isEmpty())
            throw new IllegalArgumentException("The orderBys must not be null or empty.");
        String[] orderByStrings = orderBysString.split(SEPARATOR);
        return of(orderByStrings);
    }

    @Override
    public void applyToQueryWrapper(QueryWrapper<? extends BaseModel> queryWrapper) {
        for (OrderBy orderBy : orderBys)
            orderBy.applyToQueryWrapper(queryWrapper);
    }

    @Override
    public String toString() {
        return Arrays.stream(orderBys).map(OrderBy::toString).collect(Collectors.joining(SEPARATOR));
    }
}
