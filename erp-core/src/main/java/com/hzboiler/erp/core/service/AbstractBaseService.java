package com.hzboiler.erp.core.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.hzboiler.erp.core.field.*;
import com.hzboiler.erp.core.field.annotations.OnDelete;
import com.hzboiler.erp.core.field.util.RelationFieldUtil;
import com.hzboiler.erp.core.mapper.RelationMapper;
import com.hzboiler.erp.core.model.BaseModel;
import com.hzboiler.erp.core.protocal.query.Condition;
import com.hzboiler.erp.core.protocal.query.OrderBys;
import com.hzboiler.erp.core.security.model.ModelAccessCheckUtil;
import lombok.SneakyThrows;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public abstract class AbstractBaseService<M extends BaseMapper<T>, T extends BaseModel>
        extends ServiceImpl<M, T>
        implements BaseService<T>, InitializingBean {

    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    private M baseMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public T selectById(Long id) {
        if (id == null) return null;

        List<T> records = selectByIds(List.of(id));
        return records.isEmpty() ? null : records.get(0);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<T> selectByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) return Collections.emptyList();
        ModelAccessCheckUtil.checkSelect(entityClass);
        return listByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public IPage<T> page(Long pageNum, Long pageSize, Condition condition, OrderBys orderBys) {
        ModelAccessCheckUtil.checkSelect(entityClass);
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        if (condition != null)
            condition.applyToQueryWrapper(queryWrapper);
        if (orderBys != null)
            orderBys.applyToQueryWrapper(queryWrapper);
        return super.page(new Page<>(pageNum, pageSize), queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long count(QueryWrapper<T> queryWrapper) {
        ModelAccessCheckUtil.checkSelect(entityClass);
        return super.count(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<T> selectList(QueryWrapper<T> queryWrapper) {
        ModelAccessCheckUtil.checkSelect(entityClass);
        return super.list(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<T> nameSearch(String name) {
        ModelAccessCheckUtil.checkSelect(entityClass);
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
    @Transactional(rollbackFor = Exception.class)
    public boolean createOne(T record) {
        if (record == null) return false;
        return createBatch(List.of(record));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createBatch(List<T> records) {
        if (records == null || records.isEmpty()) return false;

        // check authority for create
        ModelAccessCheckUtil.checkCreate(entityClass);

        // do create
        boolean res = saveBatch(records);

        // process one2many fields
        processOne2ManyForCreate(records);

        // process many2many fields
        processMany2ManyForCreate(records);
        return res;
    }

    @SneakyThrows(IllegalAccessException.class)
    @SuppressWarnings("unchecked")
    private void processOne2ManyForCreate(List<T> records) {
        for (Field field : RelationFieldUtil.getOne2ManyFields(entityClass)) {
            Class<BaseModel> targetClass = RelationFieldUtil.getTargetModelClass(entityClass, field);
            Field inverseField = RelationFieldUtil.getInverseField(entityClass, field);
            BaseService<BaseModel> targetService = getService(targetClass);
            for (T record : records) {
                One2Many<BaseModel> filedValue;
                try {
                    filedValue = (One2Many<BaseModel>) field.get(record);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
                if (filedValue == null) break;

                List<Command<BaseModel>> commands = filedValue.getCommands();
                if (commands == null) break;
                for (Command<BaseModel> command : commands) {
                    if (command == null) break;
                    if (Objects.requireNonNull(command.getCommandType()) == CommandType.CREATE) {
                        if (command.getRecords() == null || command.getRecords().isEmpty()) {
                            throw new IllegalArgumentException("The records of Command CREATE cannot be null or empty");
                        }
                        for (BaseModel targetRecord : command.getRecords()) {
                            inverseField.set(targetRecord, Many2One.ofId(record.getId()));
                        }
                        targetService.createBatch(command.getRecords());
                    } else {
                        throw new IllegalArgumentException("The Command " + command.getCommandType()
                                + " is not supported for one2many field in create method");
                    }
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void processMany2ManyForCreate(List<T> records) {
        for (Field field : RelationFieldUtil.getMany2ManyFields(entityClass)) {
            Class<? extends BaseModel> targetClass = RelationFieldUtil.getTargetModelClass(entityClass, field);
            for (T record : records) {
                Many2Many<BaseModel> filedValue;
                try {
                    filedValue = (Many2Many<BaseModel>) field.get(record);
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
                        RelationMapper mapper = getRelationMapper(targetClass);
                        mapper.add(entityClass, record.getId(), command.getIds());
                    } else {
                        throw new IllegalArgumentException("The Command " + command.getCommandType()
                                + " is not supported for many2many field in create method");
                    }
                }
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateById(Long id, T updateValues) {
        if (id == null || updateValues == null) return false;
        return updateByIds(List.of(id), updateValues);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateByIds(List<Long> ids, T updateValues) {
        if (ids == null || ids.isEmpty() || updateValues == null) return false;
        if (updateValues.getId() != null)
            throw new IllegalArgumentException("record's id must be null");

        // check authority for update
        ModelAccessCheckUtil.checkUpdate(entityClass);

        // construct LambdaUpdateWrapper
        LambdaUpdateWrapper<T> wrapper = new LambdaUpdateWrapper<>();
        wrapper.setEntityClass(entityClass);
        wrapper.in(T::getId, ids);

        // do update
        boolean res = update(updateValues, wrapper);

        // process one2many fields
        processOne2ManyForUpdate(ids, updateValues);

        // process many2many fields
        processMany2ManyForUpdate(ids, updateValues);

        return res;
    }

    @SneakyThrows(IllegalAccessException.class)
    @SuppressWarnings("unchecked")
    private void processOne2ManyForUpdate(List<Long> ids, T record) {
        for (Field field : RelationFieldUtil.getOne2ManyFields(entityClass)) {
            Class<BaseModel> targetClass = RelationFieldUtil.getTargetModelClass(entityClass, field);
            Field inverseField = RelationFieldUtil.getInverseField(entityClass, field);
            BaseService<BaseModel> targetService = getService(targetClass);
            One2Many<BaseModel> filedValue;
            try {
                filedValue = (One2Many<BaseModel>) field.get(record);
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
                            for (BaseModel targetRecord : command.getRecords()) {
                                inverseField.set(targetRecord, Many2One.ofId(sourceId));
                            }
                            targetService.createBatch(command.getRecords());
                        }
                        case DELETE: {
                            targetService.deleteByIds(command.getIds());
                            break;
                        }
                        case UPDATE: {
                            targetService.updateByIds(command.getIds(), command.getRecords().get(0));
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
    private void processMany2ManyForUpdate(List<Long> ids, T record) {
        for (Field field : RelationFieldUtil.getMany2ManyFields(entityClass)) {
            Class<? extends BaseModel> targetClass = RelationFieldUtil.getTargetModelClass(entityClass, field);
            Many2Many<BaseModel> filedValue;
            try {
                filedValue = (Many2Many<BaseModel>) field.get(record);
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
                            RelationMapper mapper = getRelationMapper(targetClass);
                            mapper.add(entityClass, sourceId, command.getIds());
                            break;
                        }
                        case REMOVE: {
                            RelationMapper mapper = getRelationMapper(targetClass);
                            mapper.remove(entityClass, sourceId, command.getIds());
                            break;
                        }
                        case REPLACE: {
                            RelationMapper mapper = getRelationMapper(targetClass);
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
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteById(Long id) {
        if (id == null) return false;
        return deleteByIds(List.of(id));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) return false;

        // check authority for delete
        ModelAccessCheckUtil.checkDelete(entityClass);

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
            Class<BaseModel> targetClass = RelationFieldUtil.getTargetModelClass(entityClass, field);
            Field inverseField = RelationFieldUtil.getInverseField(entityClass, field);
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
            Class<? extends BaseModel> targetClass = RelationFieldUtil.getTargetModelClass(entityClass, field);
            for (Long sourceId : ids) {
                RelationMapper mapper = getRelationMapper(targetClass);
                mapper.removeAll(entityClass, sourceId);
            }
        }
    }

    @Override
    public Class<T> getModelClass() {
        return super.getEntityClass();
    }

    @Override
    public M getMapper() {
        return baseMapper;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <S extends BaseService<AT>, AT extends BaseModel> S getService(Class<AT> modelClass) {
        return (S) BaseServiceRegistry.getService(modelClass);
    }

    @Override
    public LambdaQueryChainWrapper<T> lambdaQuery() {
        ModelAccessCheckUtil.checkSelect(entityClass);
        return ChainWrappers.lambdaQueryChain(baseMapper, entityClass);
    }

    @Override
    public void afterPropertiesSet() {
        BaseServiceRegistry.register(this);
    }
}
