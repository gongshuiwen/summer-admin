package io.summernova.admin.common.query;

import io.summernova.admin.common.util.FieldNameCheckUtil;
import lombok.Getter;

/**
 * @author gongshuiwen
 */
@Getter
public final class OrderBy {

    private final String field;
    private final OrderByType type;

    // prevent external instantiation
    private OrderBy(String field, OrderByType type) {
        this.field = field;
        this.type = type;
    }

    // ------------------------
    // Static factory methods
    // ------------------------
    public static OrderBy asc(String field) {
        FieldNameCheckUtil.checkFieldName(field);
        return new OrderBy(field, OrderByType.ASC);
    }

    public static OrderBy desc(String field) {
        FieldNameCheckUtil.checkFieldName(field);
        return new OrderBy(field, OrderByType.DESC);
    }

    @Override
    public String toString() {
        return field + " " + type;
    }

    public enum OrderByType {
        ASC,
        DESC
    }
}
