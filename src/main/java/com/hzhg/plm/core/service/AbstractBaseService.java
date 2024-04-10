package com.hzhg.plm.core.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hzhg.plm.core.entity.BaseEntity;
import com.hzhg.plm.core.fields.*;
import com.hzhg.plm.core.mapper.RelationMapper;
import com.hzhg.plm.core.mapper.RelationMapperRegistry;
import com.hzhg.plm.core.protocal.Condition;
import com.hzhg.plm.core.protocal.Query;
import com.hzhg.plm.core.security.DataAccessAuthority;
import com.hzhg.plm.core.security.DataAccessAuthorityChecker;
import lombok.SneakyThrows;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractBaseService<M extends BaseMapper<T>, T extends BaseEntity>
        extends ServiceImpl<M, T>
        implements IBaseService<T>, InitializingBean {

    public static final Map<Class<?>, IBaseService<?>> baseServiceRegistry = new ConcurrentHashMap<>();

    @Override
    public T selectById(Long id) {
        if (id == null) return null;

        List<T> entities = selectByIds(List.of(id));
        return entities.isEmpty() ? null : entities.get(0);
    }

    @Override
    public List<T> selectByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) return Collections.emptyList();
        DataAccessAuthorityChecker.check(entityClass, DataAccessAuthority.SELECT);
        return listByIds(ids);
    }

    @Override
    public IPage<T> page(Long pageNum, Long pageSize, Condition<T> condition, String sort) {
        DataAccessAuthorityChecker.check(entityClass, DataAccessAuthority.SELECT);
        IPage<T> page = new Page<>(pageNum, pageSize);
        Query<T> query = new Query<>();
        query.setSort(sort);
        query.setCondition(condition);
        return super.page(page, query.buildPageQueryWrapper());
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
    public List<T> nameSearch(String name) {
        DataAccessAuthorityChecker.check(entityClass, DataAccessAuthority.SELECT);
        Page<T> page = new Page<>(1, 7);

        if (name == null || name.isEmpty() || name.isBlank()) return list(page);

        // TODO: optimize performance by cache
        try {
            entityClass.getDeclaredField("name");
        } catch (NoSuchFieldException e) {
            return Collections.emptyList();
        }

        LambdaQueryWrapper<T> wrapper = new LambdaQueryWrapper<>(entityClass);
        wrapper.like(T::getName, name.trim());
        return list(page, wrapper);
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

        // check authority for create
        DataAccessAuthorityChecker.check(entityClass, DataAccessAuthority.CREATE);

        // do create
        boolean res = saveBatch(entities);

        // process many2many fields
        processMany2ManyForCreate(entities);
        return res;
    }

    @SuppressWarnings("unchecked")
    private void processMany2ManyForCreate(List<T> entities) {
        for (Field field : Many2Many.getMany2ManyFields(entityClass)) {
            Class<?> targetClass = Many2Many.getTargetClass(field);
            IBaseService<BaseEntity> targetService = (IBaseService<BaseEntity>) baseServiceRegistry.get(targetClass);
            for (T entity : entities) {
                Many2Many<BaseEntity> filedValue;
                try {
                    filedValue = (Many2Many<BaseEntity>) field.get(entity);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
                if (filedValue == null) break;

                List<Command<BaseEntity>> commands = filedValue.getCommands();
                if (commands == null) break;

                for (Command<BaseEntity> command : commands) {
                    if (command == null) break;
                    if (Objects.requireNonNull(command.getCommandType()) == CommandType.ADD) {
                        RelationMapper mapper = RelationMapperRegistry.getMapper(entityClass, targetClass);
                        mapper.add(entityClass, entity.getId(), command.getIds());
                    } else {
                        throw new RuntimeException();
                    }
                }
            }
        }
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

        // check authority for update
        DataAccessAuthorityChecker.check(entityClass, DataAccessAuthority.UPDATE);

        // construct LambdaUpdateWrapper
        LambdaUpdateWrapper<T> wrapper = new LambdaUpdateWrapper<>();
        wrapper.setEntityClass((Class<T>) entity.getClass());
        wrapper.in(T::getId, ids);

        // do update
        boolean res = update(entity, wrapper);

        // process many2many fields
        processMany2ManyForUpdate(ids, entity);

        return res;
    }

    @SuppressWarnings("unchecked")
    private void processMany2ManyForUpdate(List<Long> ids, T entity) {
        for (Field field : Many2Many.getMany2ManyFields(entityClass)) {
            Class<?> targetClass = Many2Many.getTargetClass(field);
            IBaseService<BaseEntity> targetService = (IBaseService<BaseEntity>) baseServiceRegistry.get(targetClass);
            Many2Many<BaseEntity> filedValue;
            try {
                filedValue = (Many2Many<BaseEntity>) field.get(entity);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            if (filedValue == null) break;

            for (Long sourceId : ids) {
                List<Command<BaseEntity>> commands = filedValue.getCommands();
                if (commands == null) break;

                for (Command<BaseEntity> command : commands) {
                    if (command == null) break;
                    switch (command.getCommandType()) {
                        case ADD: {
                            RelationMapper mapper = RelationMapperRegistry.getMapper(entityClass, targetClass);
                            mapper.add(entityClass, sourceId, command.getIds());
                            break;
                        }
                        case REMOVE: {
                            RelationMapper mapper = RelationMapperRegistry.getMapper(entityClass, targetClass);
                            mapper.remove(entityClass, sourceId, command.getIds());
                            break;
                        }
                        case REPLACE: {
                            RelationMapper mapper = RelationMapperRegistry.getMapper(entityClass, targetClass);
                            mapper.replace(entityClass, sourceId, command.getIds());
                            break;
                        }
                        default: {
                            throw new RuntimeException();
                        }
                    }
                }
            }
        }
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

        // check authority for delete
        DataAccessAuthorityChecker.check(entityClass, DataAccessAuthority.DELETE);

        // do delete
        boolean res = removeByIds(ids);

        // process many2many fields
        processMany2manyForDelete(ids);
        return res;
    }

    private void processMany2manyForDelete(List<Long> ids) {
        for (Field field : Many2Many.getMany2ManyFields(entityClass)) {
            Class<?> targetClass = Many2Many.getTargetClass(field);
            for (Long sourceId : ids) {
                RelationMapper mapper = RelationMapperRegistry.getMapper(entityClass, targetClass);
                mapper.removeAll(entityClass, sourceId);
            }
        }
    }

    @Override
    public void afterPropertiesSet() {
        if (entityClass == null) {
            throw new RuntimeException();
        }
        baseServiceRegistry.put(entityClass, this);
    }
}
