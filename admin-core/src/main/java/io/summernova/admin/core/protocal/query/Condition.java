package io.summernova.admin.core.protocal.query;

import lombok.Getter;

/**
 * @author gongshuiwen
 */
@Getter
public abstract class Condition implements QueryWrapperAdapter {

    final String operator;

    Condition(String operator) {
        this.operator = operator;
    }

    static void checkField(String field) {
        FieldNameCheckUtil.checkFieldName(field);
    }

    public abstract String getSql();
}
