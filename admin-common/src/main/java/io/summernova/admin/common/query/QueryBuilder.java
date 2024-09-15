package io.summernova.admin.common.query;

import java.util.ArrayList;
import java.util.List;

/**
 * @author gongshuiwen
 */
public final class QueryBuilder {

    private Long limit;
    private Long offset;
    private Condition condition;
    private List<OrderBy> orderBys;

    // prevent external instantiation
    private QueryBuilder() {
    }

    public static QueryBuilder newBuilder() {
        return new QueryBuilder();
    }

    public QueryBuilder condition(Condition condition) {
        this.condition = condition;
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

    public QueryBuilder limit(Long limit) {
        this.limit = limit;
        return this;
    }

    public QueryBuilder offset(Long offset) {
        this.offset = offset;
        return this;
    }

    public Query build() {
        if (orderBys == null) {
            return Query.of(condition, null, limit, offset);
        } else {
            return Query.of(condition, OrderBys.of(orderBys.toArray(new OrderBy[0])), limit, offset);
        }
    }
}
