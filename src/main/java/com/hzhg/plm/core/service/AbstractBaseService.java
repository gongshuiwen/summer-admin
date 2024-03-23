package com.hzhg.plm.core.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hzhg.plm.core.entity.BaseEntity;
import com.hzhg.plm.core.protocal.Condition;
import com.hzhg.plm.core.protocal.Query;
import com.hzhg.plm.core.security.DataAccessAuthority;
import com.hzhg.plm.core.security.DataAccessAuthorityChecker;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

public abstract class AbstractBaseService<M extends BaseMapper<T>, T extends BaseEntity>
        extends ServiceImpl<M, T>
        implements IBaseService<T> {

    @Override
    public T selectById(Long id) {
        if (id == null) return null;
        return selectByIds(List.of(id)).get(0);
    }

    @Override
    public List<T> selectByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) return Collections.emptyList();
        DataAccessAuthorityChecker.check(entityClass, DataAccessAuthority.SELECT);
        return listByIds(ids);
    }

    @Override
    public Long count(Condition<T> condition) {
        DataAccessAuthorityChecker.check(entityClass, DataAccessAuthority.SELECT);
        if (condition == null) return super.count();
        QueryWrapper<T> wrapper = new QueryWrapper<>();
        condition.applyToQueryWrapper(wrapper);
        return super.count(wrapper);
    }

    @Override
    @Transactional
    public boolean createOne(T entity) {
        if (entity == null) return false;
        return createBatch(List.of(entity));
    }

    @Override
    @Transactional
    public boolean createBatch(List<T> entities) {
        if (entities == null || entities.isEmpty()) return false;
        DataAccessAuthorityChecker.check(entityClass, DataAccessAuthority.CREATE);
        return saveBatch(entities);
    }

    @Override
    @Transactional
    public boolean updateById(Long id, T entity) {
        if (id == null || entity == null) return false;
        return updateByIds(List.of(id), entity);
    }

    @Override
    @Transactional
    @SuppressWarnings("unchecked")
    public boolean updateByIds(List<Long> ids, T entity) {
        if (ids == null || ids.isEmpty() || entity == null) return false;
        DataAccessAuthorityChecker.check(entityClass, DataAccessAuthority.UPDATE);
        LambdaUpdateWrapper<T> wrapper = new LambdaUpdateWrapper<>();
        wrapper.setEntityClass((Class<T>) entity.getClass());
        wrapper.in(T::getId, ids);
        return update(entity, wrapper);
    }

    @Override
    @Transactional
    public boolean deleteById(Long id) {
        if (id == null) return false;
        return deleteByIds(List.of(id));
    }

    @Override
    @Transactional
    public boolean deleteByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) return false;
        DataAccessAuthorityChecker.check(entityClass, DataAccessAuthority.DELETE);
        return removeByIds(ids);
    }
}
