package com.hzhg.plm.query;

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
public class Query<C> {

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

    Long pageSize = 20L;
    Long pageNum = 1L;
    List<Domain> domains;
    String order;

    public QueryWrapper<C> buildPageQueryWrapper() {
        QueryWrapper<C> queryWrapper = new QueryWrapper<>();
        buildDomains(queryWrapper);
        buildSorts(queryWrapper);
        return queryWrapper;
    }

    public QueryWrapper<C> buildCountQueryWrapper() {
        QueryWrapper<C> queryWrapper = new QueryWrapper<>();
        buildDomains(queryWrapper);
        return queryWrapper;
    }

    private void buildDomains(QueryWrapper<C> queryWrapper) {
        Class<?> clazz = queryWrapper.getEntityClass();
        for (Domain domain : domains) {
            checkColumn(clazz, domain.getColumn());
            try {
                method.invoke(queryWrapper, true, domain.getColumn(), domain.getSqlKeyword(), domain.getValue());
            } catch (InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void buildSorts(QueryWrapper<C> queryWrapper) {
        if (getOrder() == null) {
            return;
        }

        for (String s : getOrder().split(",")) {
            if (s.endsWith(" asc")) {
                queryWrapper.orderByAsc(s.substring(0, s.length() - 4));
            } else if (s.endsWith(" desc")) {
                queryWrapper.orderByDesc(s.substring(0, s.length() - 5));
            } else {
                queryWrapper.orderByAsc(s);
            }
        }
    }

    private void checkColumn(Class<?> clazz, String column) {
        // TODO: implement check column existing in entity class with cache
    }
}