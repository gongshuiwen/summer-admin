package com.hzboiler.erp.core.protocal.query;

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
        if (field == null || field.isBlank())
            throw new IllegalArgumentException("The field must not be null or blank.");
    }

    public abstract String getSql();
}
