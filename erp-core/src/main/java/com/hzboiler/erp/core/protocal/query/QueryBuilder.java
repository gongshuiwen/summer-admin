package com.hzboiler.erp.core.protocal.query;

import java.util.ArrayList;
import java.util.List;

/**
 * @author gongshuiwen
 */
public class QueryBuilder {

    private Long pageNum;
    private Long pageSize;
    private Condition condition;
    private List<OrderBy> orderBys;

    // prevent external instantiation
    private QueryBuilder() {}

    public static QueryBuilder newBuilder() {
        return new QueryBuilder();
    }

    public QueryBuilder pageNum(Long pageNum) {
        this.pageNum = pageNum;
        return this;
    }

    public QueryBuilder pageSize(Long pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public QueryBuilder OrderByAsc(String field) {
        if (orderBys == null)
            orderBys = new ArrayList<>();
        orderBys.add(OrderBy.asc(field));
        return this;
    }

    public QueryBuilder OrderByDesc(String field) {
        if (orderBys == null)
            orderBys = new ArrayList<>();
        this.orderBys.add(OrderBy.desc(field));
        return this;
    }

    public QueryBuilder condition(Condition condition) {
        this.condition = condition;
        return this;
    }

    public Query build() {
        if (pageNum == null)
            pageNum = 1L;
        if (pageSize == null)
            pageSize = 20L;

        if (orderBys == null) {
            return Query.of(pageNum, pageSize, condition, null);
        } else {
            return Query.of(pageNum, pageSize, condition, OrderBys.of(orderBys.toArray(new OrderBy[0])));
        }
    }
}
