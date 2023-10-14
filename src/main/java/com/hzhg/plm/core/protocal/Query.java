package com.hzhg.plm.core.protocal;

import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.enums.SqlKeyword;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

@Getter
@Setter
public class Query<T> {

    private static Method method;
    static {
        try {
            method = AbstractWrapper.class.getDeclaredMethod("addCondition",
                    boolean.class, Object.class, SqlKeyword.class, Object.class);
            method.setAccessible(true);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private Long pageSize = 20L;
    private Long pageNum = 1L;
    private String sort;
    private List<Domain> domains;

    private QueryWrapper<T> queryWrapper;
    private Class<T> entityClass;

    public QueryWrapper<T> buildPageQueryWrapper() {
        queryWrapper = new QueryWrapper<>();
        buildDomains();
        buildSorts();
        return queryWrapper;
    }

    public QueryWrapper<T> buildCountQueryWrapper() {
        queryWrapper = new QueryWrapper<>();
        buildDomains();
        return queryWrapper;
    }

    private void buildDomains() {
        for (Domain domain : domains) {
            checkColumn(domain.getColumn());
            try {
                method.invoke(queryWrapper, true, domain.getColumn(), domain.getSqlKeyword(), domain.getValue());
            } catch (InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
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