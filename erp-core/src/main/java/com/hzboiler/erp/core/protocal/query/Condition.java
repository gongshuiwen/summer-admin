package com.hzboiler.erp.core.protocal.query;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hzboiler.erp.core.model.BaseModel;
import lombok.Getter;

/**
 * @author gongshuiwen
 */
@Getter
public abstract class Condition {

    private final String operator;

    Condition(String operator) {
        this.operator = operator;
    }

    public abstract void applyToQueryWrapper(QueryWrapper<? extends BaseModel> queryWrapper);

    public abstract <T extends BaseModel> QueryWrapper<T> toQueryWrapper(Class<T> clazz);

    public abstract String getSql();
}
