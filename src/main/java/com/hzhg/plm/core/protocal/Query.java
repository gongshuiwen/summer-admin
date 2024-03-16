package com.hzhg.plm.core.protocal;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Query<T> {

    private Long pageSize = 20L;
    private Long pageNum = 1L;
    private String sort;
    private Condition<T> condition;

    @JsonIgnore
    private QueryWrapper<T> queryWrapper;

    @JsonIgnore
    private Class<T> entityClass;

    public QueryWrapper<T> buildPageQueryWrapper() {
        queryWrapper = new QueryWrapper<>();
        buildCondition();
        buildSorts();
        return queryWrapper;
    }

    public QueryWrapper<T> buildCountQueryWrapper() {
        queryWrapper = new QueryWrapper<>();
        buildCondition();
        return queryWrapper;
    }

    private void buildCondition() {
        if (getCondition() == null) {
            return;
        }
        condition.applyToQueryWrapper(queryWrapper);
    }

    private void buildSorts() {
        if (getSort() == null) {
            return;
        }

        for (String s : getSort().split(",")) {
            if (s.endsWith(" asc")) {
                String column = s.substring(0, s.length() - 4);
                checkColumn(column);
                queryWrapper.orderByAsc(column);
            } else if (s.endsWith(" desc")) {
                String column = s.substring(0, s.length() - 5);
                checkColumn(column);
                queryWrapper.orderByDesc(column);
            } else {
                checkColumn(s);
                queryWrapper.orderByAsc(s);
            }
        }
    }

    private void checkColumn(String column) {
        // TODO: implement check column existing in entity class with cache
    }
}