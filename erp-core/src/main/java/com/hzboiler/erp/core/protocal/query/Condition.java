package com.hzboiler.erp.core.protocal.query;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hzboiler.erp.core.model.BaseModel;
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
        if (field == null || field.isBlank())
            throw new IllegalArgumentException("The field must not be null or blank.");
    }

    public <T extends BaseModel> QueryWrapper<T> toQueryWrapper(Class<T> clazz) {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntityClass(clazz);
        applyToQueryWrapper(queryWrapper);
        return queryWrapper;
    }

    public abstract void applyToQueryWrapper(QueryWrapper<? extends BaseModel> queryWrapper);

    public abstract String getSql();
}
