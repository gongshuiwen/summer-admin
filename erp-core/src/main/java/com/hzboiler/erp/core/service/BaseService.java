package com.hzboiler.erp.core.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hzboiler.erp.core.model.BaseModel;
import com.hzboiler.erp.core.protocal.query.OrderBy;
import com.hzboiler.erp.core.protocal.query.Condition;

import java.util.List;

public interface BaseService<T extends BaseModel> {

    T selectById(Long id);

    List<T> selectByIds(List<Long> ids);

    default IPage<T> page(Long pageNum, Long pageSize) {
        return page(pageNum, pageSize, null, null);
    }

    default IPage<T> page(Long pageNum, Long pageSize, String sort) {
        return page(pageNum, pageSize, sort, null);
    }

    IPage<T> page(Long pageNum, Long pageSize, String sorts, Condition condition);

    default Long count(Condition condition) {
        if (condition == null)
            return count((QueryWrapper<T>) null);
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        condition.applyToQueryWrapper(queryWrapper);
        return count(queryWrapper);
    }

    Long count(QueryWrapper<T> queryWrapper);

    default T selectOne(Condition condition) {
        return selectOne(condition, null);
    }

    default T selectOne(Condition condition, List<OrderBy<T>> orderBys) {
        QueryWrapper<T> queryWrapper = condition.toQueryWrapper(getModelClass());

        if (orderBys != null && !orderBys.isEmpty()) {
            for (OrderBy<T> orderBy : orderBys) {
                orderBy.applyToQueryWrapper(queryWrapper);
            }
        }
        return selectOne(queryWrapper);
    }

    default T selectOne(QueryWrapper<T> queryWrapper) {
        List<T> list = selectList(queryWrapper);
        return list.isEmpty() ? null : list.get(0);
    }

    default List<T> selectList(Condition condition) {
        return selectList(condition, 0L, 0L, null);
    }

    default List<T> selectList(Condition condition, Long limit) {
        return selectList(condition, limit, 0L, null);
    }

    default List<T> selectList(Condition condition, Long limit, Long offset) {
        return selectList(condition, limit, offset, null);
    };

    default List<T> selectList(Condition condition, Long limit, Long offset, List<OrderBy<T>> orderBys) {
        if (limit == null || limit < 0)
            throw new IllegalArgumentException("limit must not be null or < 0!");
        if (offset == null || offset < 0)
            throw new IllegalArgumentException("offset must not be null or < 0!");

        QueryWrapper<T> queryWrapper = condition.toQueryWrapper(getModelClass());
        if (limit > 0) {
            queryWrapper.last("limit " + limit);
            if (offset > 0) {
                queryWrapper.last("offset " + offset);
            }
        }

        if (orderBys != null && !orderBys.isEmpty()) {
            for (OrderBy<T> orderBy : orderBys) {
                orderBy.applyToQueryWrapper(queryWrapper);
            }
        }

        return selectList(queryWrapper);
    }

    List<T> selectList(QueryWrapper<T> queryWrapper);

    List<T> nameSearch(String name);

    boolean createOne(T entity);

    boolean createBatch(List<T> records);

    boolean updateById(Long id, T entity);

    boolean updateByIds(List<Long> ids, T entity);

    boolean deleteById(Long id);

    boolean deleteByIds(List<Long> ids);

    Class<T> getModelClass();

    BaseMapper<T> getMapper();

    <AT extends BaseModel> BaseService<AT> getService(Class<AT> modelClass);
}
