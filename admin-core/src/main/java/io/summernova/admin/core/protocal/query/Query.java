package io.summernova.admin.core.protocal.query;

import lombok.Getter;

/**
 * @author gongshuiwen
 */
@Getter
public class Query {

    private final Condition condition;
    private final OrderBys orderBys;
    private final Long limit;
    private final Long offset;

    // prevent external instantiation
    private Query(Condition condition, OrderBys orderBys, Long limit, Long offset) {
        this.condition = condition;
        this.orderBys = orderBys;
        this.limit = limit;
        this.offset = offset;
    }

    public static Query of(Condition condition, OrderBys orderBys, Long limit, Long offset) {
        return new Query(condition, orderBys, limit, offset);
    }
}
