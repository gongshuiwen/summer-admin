package io.summernova.admin.common.query;

import io.summernova.admin.common.util.FieldNameCheckUtil;
import lombok.Getter;

/**
 * @author gongshuiwen
 */
@Getter
public abstract class Condition {

    final String operator;

    Condition(String operator) {
        this.operator = operator;
    }

    static void checkField(String field) {
        FieldNameCheckUtil.checkFieldName(field);
    }

    public abstract String getSql();
}
