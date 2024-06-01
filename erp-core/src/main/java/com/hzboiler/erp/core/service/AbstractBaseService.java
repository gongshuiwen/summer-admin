package com.hzboiler.erp.core.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hzboiler.erp.core.field.*;
import com.hzboiler.erp.core.field.util.RelationFieldUtil;
import com.hzboiler.erp.core.mapper.RelationMapper;
import com.hzboiler.erp.core.mapper.RelationMapperRegistry;
import com.hzboiler.erp.core.protocal.query.Condition;
import com.hzboiler.erp.core.protocal.query.Query;
import com.hzboiler.erp.core.security.DataAccessAuthority;
import com.hzboiler.erp.core.security.DataAccessAuthorityChecker;
import com.hzboiler.erp.core.model.BaseModel;
import com.hzboiler.erp.core.field.annotations.OnDelete;
import lombok.SneakyThrows;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public abstract class AbstractBaseService<M extends BaseMapper<T>, T extends BaseModel>
        extends ServiceImpl<M, T>
        implements BaseService<T>, InitializingBean {

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
    public IPage<T> page(Long pageNum, Long pageSize, String sorts, Condition condition) {
        DataAccessAuthorityChecker.check(entityClass, DataAccessAuthority.SELECT);
        Query<T> query = new Query<>(entityClass, pageNum, pageSize, sorts, condition);
        return super.page(query.getPage(), query.getQueryWrapper());
    }

    @Override
    public Long count(QueryWrapper<T> queryWrapper) {
        DataAccessAuthorityChecker.check(entityClass, DataAccessAuthority.SELECT);
        return super.count(queryWrapper);
    }

    @Override
    public List<T> selectList(QueryWrapper<T> queryWrapper) {
        DataAccessAuthorityChecker.check(entityClass, DataAccessAuthority.SELECT);
        return super.list(queryWrapper);
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

        // process one2many fields
        processOne2ManyForCreate(entities);

        // process many2many fields
        processMany2ManyForCreate(entities);
        return res;
    }

    @SneakyThrows(IllegalAccessException.class)
    @SuppressWarnings("unchecked")
    private void processOne2ManyForCreate(List<T> entities) {
        for (Field field : RelationFieldUtil.getOne2ManyFields(entityClass)) {
            Class<BaseModel> targetClass = RelationFieldUtil.getTargetModelClass(field);
            Field inverseField = RelationFieldUtil.getInverseField(field);
            BaseService<BaseModel> targetService = getService(targetClass);
            for (T entity : entities) {
                One2Many<BaseModel> filedValue;
                try {
                    filedValue = (One2Many<BaseModel>) field.get(entity);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
                if (filedValue == null) break;

                List<Command<BaseModel>> commands = filedValue.getCommands();
                if (commands == null) break;
                for (Command<BaseModel> command : commands) {
                    if (command == null) break;
                    if (Objects.requireNonNull(command.getCommandType()) == CommandType.CREATE) {
                        if (command.getEntities() == null || command.getEntities().isEmpty()) {
                            throw new IllegalArgumentException("The entities of Command CREATE cannot be null or empty");
                        }
                        for (BaseModel targetEntity : command.getEntities()) {
                            inverseField.set(targetEntity, Many2One.ofId(entity.getId()));
                        }
                        targetService.createBatch(command.getEntities());
                    } else {
                        throw new IllegalArgumentException("The Command " + command.getCommandType()
                                + " is not supported for one2many field in create method");
                    }
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void processMany2ManyForCreate(List<T> entities) {
        for (Field field : RelationFieldUtil.getMany2ManyFields(entityClass)) {
            Class<?> targetClass = RelationFieldUtil.getTargetModelClass(field);
            for (T entity : entities) {
                Many2Many<BaseModel> filedValue;
                try {
                    filedValue = (Many2Many<BaseModel>) field.get(entity);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
                if (filedValue == null) break;

                List<Command<BaseModel>> commands = filedValue.getCommands();
                if (commands == null) break;

                for (Command<BaseModel> command : commands) {
                    if (command == null) break;
                    if (Objects.requireNonNull(command.getCommandType()) == CommandType.ADD) {
                        if (command.getIds() == null || command.getIds().isEmpty()) {
                            throw new IllegalArgumentException("The ids of Command ADD cannot be null or empty");
                        }
                        RelationMapper mapper = RelationMapperRegistry.getMapper(entityClass, targetClass);
                        mapper.add(entityClass, entity.getId(), command.getIds());
                    } else {
                        throw new IllegalArgumentException("The Command " + command.getCommandType()
                                + " is not supported for many2many field in create method");
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

        // process one2many fields
        processOne2ManyForUpdate(ids, entity);

        // process many2many fields
        processMany2ManyForUpdate(ids, entity);

        return res;
    }

    @SneakyThrows(IllegalAccessException.class)
    @SuppressWarnings("unchecked")
    private void processOne2ManyForUpdate(List<Long> ids, T entity) {
        for (Field field : RelationFieldUtil.getOne2ManyFields(entityClass)) {
            Class<BaseModel> targetClass = RelationFieldUtil.getTargetModelClass(field);
            Field inverseField = RelationFieldUtil.getInverseField(field);
            BaseService<BaseModel> targetService = getService(targetClass);
            One2Many<BaseModel> filedValue;
            try {
                filedValue = (One2Many<BaseModel>) field.get(entity);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            if (filedValue == null) break;

            for (Long sourceId : ids) {
                List<Command<BaseModel>> commands = filedValue.getCommands();
                if (commands == null) break;

                for (Command<BaseModel> command : commands) {
                    if (command == null) break;
                    switch (command.getCommandType()) {
                        case CREATE: {
                            for (BaseModel targetEntity : command.getEntities()) {
                                inverseField.set(targetEntity, Many2One.ofId(sourceId));
                            }
                            targetService.createBatch(command.getEntities());
                        }
                        case DELETE: {
                            targetService.deleteByIds(command.getIds());
                            break;
                        }
                        case UPDATE: {
                            targetService.updateByIds(command.getIds(), command.getEntities().get(0));
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

    @SuppressWarnings("unchecked")
    private void processMany2ManyForUpdate(List<Long> ids, T entity) {
        for (Field field : RelationFieldUtil.getMany2ManyFields(entityClass)) {
            Class<?> targetClass = RelationFieldUtil.getTargetModelClass(field);
            Many2Many<BaseModel> filedValue;
            try {
                filedValue = (Many2Many<BaseModel>) field.get(entity);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            if (filedValue == null) break;

            for (Long sourceId : ids) {
                List<Command<BaseModel>> commands = filedValue.getCommands();
                if (commands == null) break;

                for (Command<BaseModel> command : commands) {
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

        // process one2many fields
        processOne2manyForDelete(ids);

        // process many2many fields
        processMany2manyForDelete(ids);
        return res;
    }

    private void processOne2manyForDelete(List<Long> ids) {
        for (Field field : RelationFieldUtil.getOne2ManyFields(entityClass)) {
            Class<BaseModel> targetClass = RelationFieldUtil.getTargetModelClass(field);
            Field inverseField = RelationFieldUtil.getInverseField(field);
            AbstractBaseService<BaseMapper<BaseModel>, BaseModel> targetService =
                    (AbstractBaseService<BaseMapper<BaseModel>, BaseModel>) getService(targetClass);
            OnDelete.Type onDeleteType = OnDelete.Type.RESTRICT;
            OnDelete onDelete = inverseField.getDeclaredAnnotation(OnDelete.class);
            if (onDelete != null) onDeleteType = onDelete.value();
            switch (onDeleteType) {
                case RESTRICT: {
                    if (targetService.countByMany2OneIds(inverseField, ids) > 0) {
                        // TODO: optimize exception type
                        throw new RuntimeException("Records can't be deleted, because the " + field.getName() + "field is not empty.");
                    }
                }
                case CASCADE: {
                    List<Long> targetIds = targetService.selectIdsByMany2OneIds(inverseField, ids);
                    if (!targetIds.isEmpty()) {
                        targetService.deleteByIds(targetIds);
                    }
                    break;
                }
                case SET_NULL: {
                    List<Long> targetIds = targetService.selectIdsByMany2OneIds(inverseField, ids);
                    if (!targetIds.isEmpty()) {
                        targetService.updateMany2OneToNullByIds(inverseField, targetIds);
                    }
                    break;
                }
                default: {
                    throw new RuntimeException();
                }
            }
        }
    }

    protected long countByMany2OneIds(Field field, List<Long> ids) {
        if (ids == null || ids.isEmpty()) return 0;
        QueryWrapper<T> wrapper = new QueryWrapper<>();
        wrapper.in(toColumnName(field), ids);
        return count(wrapper);
    }

    protected List<Long> selectIdsByMany2OneIds(Field field, List<Long> ids) {
        if (ids == null || ids.isEmpty()) return Collections.emptyList();
        QueryWrapper<T> wrapper = new QueryWrapper<>();
        wrapper.in(toColumnName(field), ids);
        return list(wrapper).stream().map(BaseModel::getId).toList();
    }

    protected void updateMany2OneToNullByIds(Field field, List<Long> ids) {
        if (ids == null || ids.isEmpty()) return;
        UpdateWrapper<T> wrapper = new UpdateWrapper<>();
        wrapper.in("id", ids);
        wrapper.set(toColumnName(field), null);
        update(wrapper);
    }

    private String toColumnName(Field field) {
        return field.getName().replaceAll("(.)(\\p{Upper})", "$1_$2").toLowerCase();
    }

    private void processMany2manyForDelete(List<Long> ids) {
        for (Field field : RelationFieldUtil.getMany2ManyFields(entityClass)) {
            Class<?> targetClass = RelationFieldUtil.getTargetModelClass(field);
            for (Long sourceId : ids) {
                RelationMapper mapper = RelationMapperRegistry.getMapper(entityClass, targetClass);
                mapper.removeAll(entityClass, sourceId);
            }
        }
    }

    @Override
    public Class<T> getModelClass() {
        return super.getEntityClass();
    }

    @Override
    public BaseMapper<T> getMapper() {
        return baseMapper;
    }

    @Override
    public <AT extends BaseModel> BaseService<AT> getService(Class<AT> modelClass) {
        return BaseServiceRegistry.getService(modelClass);
    }

    @Override
    public void afterPropertiesSet() {
        BaseServiceRegistry.register(this);
    }
}
