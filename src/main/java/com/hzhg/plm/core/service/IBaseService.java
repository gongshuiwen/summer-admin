package com.hzhg.plm.core.service;

import com.hzhg.plm.core.entity.BaseEntity;
import com.hzhg.plm.core.protocal.Condition;

import java.util.List;

public interface IBaseService<T extends BaseEntity> {

    T selectById(Long id);

    List<T> selectByIds(List<Long> ids);

    Long count(Condition<T> condition);

    boolean createOne(T entity);

    boolean createBatch(List<T> entities);

    boolean updateById(Long id, T entity);

    boolean updateByIds(List<Long> ids, T entity);

    boolean deleteById(Long id);

    boolean deleteByIds(List<Long> ids);
}
