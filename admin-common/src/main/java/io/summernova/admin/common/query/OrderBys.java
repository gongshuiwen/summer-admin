package io.summernova.admin.common.query;

import lombok.Getter;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author gongshuiwen
 */
@Getter
public final class OrderBys {

    public static final String SEPARATOR = ",";
    public static final String DESC_PREFIX = "_";

    private final OrderBy[] orderBys;

    // prevent external instantiation
    private OrderBys(OrderBy[] orderBys) {
        if (orderBys == null || orderBys.length == 0)
            throw new IllegalArgumentException("The orderBys must not be null or empty.");
        this.orderBys = orderBys;
    }

    // ------------------------
    // Static factory methods
    // ------------------------
    public static OrderBys of(OrderBy... orderBys) {
        return new OrderBys(orderBys);
    }

    public static OrderBys of(String orderBysString) {
        if (orderBysString == null || orderBysString.isEmpty())
            throw new IllegalArgumentException("The orderBys must not be null or empty.");
        String[] orderByStrings = orderBysString.split(SEPARATOR);
        return of(orderByStrings);
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

    @Override
    public String toString() {
        return Arrays.stream(orderBys).map(OrderBy::toString).collect(Collectors.joining(SEPARATOR));
    }
}
