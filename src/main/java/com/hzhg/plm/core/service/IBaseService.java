package com.hzhg.plm.core.service;

import com.hzhg.plm.core.entity.BaseEntity;

import java.util.List;

public interface IBaseService<T extends BaseEntity> {

    T selectById(Long id);

    List<T> selectByIds(List<Long> ids);

    boolean create(T entity);

    boolean createBatch(List<T> entities);

    boolean updateById(Long id, T entity);

    boolean updateByIds(List<Long> ids, T entity);

    boolean deleteById(Long id);

    boolean deleteByIds(List<Long> ids);
}
