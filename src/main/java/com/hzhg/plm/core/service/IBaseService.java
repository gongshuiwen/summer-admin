package com.hzhg.plm.core.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hzhg.plm.core.entity.BaseEntity;
import com.hzhg.plm.core.protocal.Condition;

import java.util.List;

public interface IBaseService<T extends BaseEntity> {

    T selectById(Long id);

    List<T> selectByIds(List<Long> ids);

    default IPage<T> page(Long pageNum, Long pageSize) {
        return page(pageNum, pageSize, null, null);
    }

    default IPage<T> page(Long pageNum, Long pageSize, Condition<T> condition) {
        return page(pageNum, pageSize, condition, null);
    }

    default IPage<T> page(Long pageNum, Long pageSize, String sort) {
        return page(pageNum, pageSize, null, sort);
    }

    IPage<T> page(Long pageNum, Long pageSize, Condition<T> condition, String sort);

    Long count(Condition<T> condition);

    List<T> nameSearch(String name);

    boolean createOne(T entity);

    boolean createBatch(List<T> entities);

    boolean updateById(Long id, T entity);

    boolean updateByIds(List<Long> ids, T entity);

    boolean deleteById(Long id);

    boolean deleteByIds(List<Long> ids);
}
