package com.hzboiler.erp.core.protocal.query;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hzboiler.erp.core.model.BaseModel;

public record Query<T extends BaseModel>(
        @JsonIgnore Class<T> modelClass,
        Long pageNum,
        Long pageSize,
        String sorts,
        Condition condition
) {

    public IPage<T> getPage() {
        return new Page<>(pageNum, pageSize);
    }

    public QueryWrapper<T> getQueryWrapper() {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        buildCondition(queryWrapper);
        buildSorts(queryWrapper);
        return queryWrapper;
    }

    private void buildCondition(QueryWrapper<T> queryWrapper) {
        if (condition == null) {
            return;
        }
        condition.applyToQueryWrapper(queryWrapper);
    }

    private void buildSorts(QueryWrapper<T> queryWrapper) {
        if (sorts == null) {
            return;
        }

        for (String s : sorts.split(",")) {
            if (s.startsWith("_")) {
                String fieldName = s.substring(1);
                checkField(fieldName);
                queryWrapper.orderByDesc(fieldName);
            } else {
                checkField(s);
                queryWrapper.orderByAsc(s);
            }
        }
    }

    private void checkField(String fieldName) {
        // TODO: implement check field existing in entity class with cache
    }
}
