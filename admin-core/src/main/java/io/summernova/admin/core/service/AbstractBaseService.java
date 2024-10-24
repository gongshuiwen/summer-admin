package io.summernova.admin.core.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import io.summernova.admin.common.query.Condition;
import io.summernova.admin.common.query.OrderBys;
import io.summernova.admin.core.dal.mapper.BaseMapper;
import io.summernova.admin.core.dal.mapper.RelationMapper;
import io.summernova.admin.core.dal.query.adapter.ConditionQueryWrapperAdapter;
import io.summernova.admin.core.dal.query.adapter.OrderBysQueryWrapperAdapter;
import io.summernova.admin.core.domain.field.*;
import io.summernova.admin.core.domain.annotations.Many2OneField;
import io.summernova.admin.core.domain.annotations.OnDeleteType;
import io.summernova.admin.core.domain.util.RelationFieldUtil;
import io.summernova.admin.core.domain.model.BaseModel;
import jakarta.annotation.PostConstruct;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.*;

/**
 * @author gongshuiwen
 */
@Slf4j
public abstract class AbstractBaseService<T extends BaseModel> implements BaseService<T> {

    private static final int DEFAULT_BATCH_SIZE = 1000;
    private final Object modelClassLock = new Object();
    private final Object baseMapperClassLock = new Object();
    private final Object baseMapperLock = new Object();
    // cache for modelClass
    private volatile Class<T> modelClass;
    // cache for baseMapperClass
    private volatile Class<BaseMapper<T>> baseMapperClass;
    // cache for BaseMapper<T>
    private volatile BaseMapper<T> baseMapper;

    @Override
    public T selectById(Long id) {
        if (id == null) return null;

        List<T> records = getBaseMapper().selectBatchIds(List.of(id));
        return records.isEmpty() ? null : records.get(0);
    }

    @Override
    public List<T> selectByIds(Collection<Long> ids) {
        if (ids == null || ids.isEmpty()) return Collections.emptyList();
        getContext().checkModelSelect(getModelClass());
        return getBaseMapper().selectBatchIds(ids);
    }

    @Override
    public IPage<T> selectPage(Long pageNum, Long pageSize, Condition condition, OrderBys orderBys) {
        getContext().checkModelSelect(getModelClass());
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        if (condition != null)
            ConditionQueryWrapperAdapter.applyConditionToQueryWrapper(condition, queryWrapper);
        if (orderBys != null)
            OrderBysQueryWrapperAdapter.applyOrderBysToQueryWrapper(orderBys, queryWrapper);
        return getBaseMapper().selectPage(new Page<>(pageNum, pageSize), queryWrapper);
    }

    @Override
    public Long selectCount(QueryWrapper<T> queryWrapper) {
        getContext().checkModelSelect(getModelClass());
        return SqlHelper.retCount(getBaseMapper().selectCount(queryWrapper));
    }

    @Override
    public List<T> selectList(QueryWrapper<T> queryWrapper) {
        getContext().checkModelSelect(getModelClass());
        return getBaseMapper().selectList(queryWrapper);
    }

    @Override
    public List<T> nameSearch(String name) {
        getContext().checkModelSelect(getModelClass());
        Page<T> page = new Page<>(1, 7);

        if (name == null || name.isEmpty() || name.isBlank())
            return getBaseMapper().selectList(page, Wrappers.emptyWrapper());

        // TODO: optimize performance by cache
        try {
            getModelClass().getDeclaredField("name");
        } catch (NoSuchFieldException e) {
            return Collections.emptyList();
        }

        LambdaQueryWrapper<T> queryWrapper = new LambdaQueryWrapper<>(getModelClass());
        queryWrapper.like(T::getName, name.trim());
        return getBaseMapper().selectList(page, queryWrapper);
    }

    @Override
    public boolean createOne(T record) {
        if (record == null) return false;
        return createBatch(List.of(record));
    }

    @Override
    public boolean createBatch(List<T> records) {
        if (records == null || records.isEmpty()) return false;

        // check authority for create
        getContext().checkModelCreate(getModelClass());

        // do create
        boolean res = saveBatch(records);

        // process one2many fields
        processOne2ManyForCreate(records);

        // process many2many fields
        processMany2ManyForCreate(records);
        return res;
    }

    protected boolean saveBatch(Collection<T> records) {
        // TODO: try some other implementation to improve batch insert performance, the way deprecated is a sample of mybatis-plus.
        //  because this only supports spring managed transaction, so the saving is not atomic!!!
//        String sqlStatement = SqlHelper.getSqlStatement(getBaseMapperClass(), SqlMethod.INSERT_ONE);
//        SqlSessionFactory sqlSessionFactory = getContext().getSqlSessionFactory();
//        return SqlHelper.executeBatch(sqlSessionFactory, mybatisLog, records, DEFAULT_BATCH_SIZE,
//              (sqlSession, entity) -> sqlSession.insert(sqlStatement, entity));
        for (T record : records) getBaseMapper().insert(record);
        return true;
    }

    @SneakyThrows(IllegalAccessException.class)
    @SuppressWarnings("unchecked")
    private void processOne2ManyForCreate(List<T> records) {
        for (Field field : RelationFieldUtil.getOne2ManyFields(getModelClass())) {
            Class<BaseModel> targetClass = RelationFieldUtil.getTargetModelClass(getModelClass(), field);
            Field inverseField = RelationFieldUtil.getInverseField(getModelClass(), field);
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
        for (Field field : RelationFieldUtil.getMany2ManyFields(getModelClass())) {
            Class<? extends BaseModel> targetClass = RelationFieldUtil.getTargetModelClass(getModelClass(), field);
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
                        RelationMapper mapper = getRelationMapper(field);
                        mapper.add(record.getId(), command.getIds());
                    } else {
                        throw new IllegalArgumentException("The Command " + command.getCommandType()
                                + " is not supported for many2many field in create method");
                    }
                }
            }
        }
    }

    @Override
    public boolean updateById(Long id, T updateValues) {
        if (id == null || updateValues == null) return false;
        return updateByIds(List.of(id), updateValues);
    }

    @Override
    public boolean updateByIds(List<Long> ids, T updateValues) {
        if (ids == null || ids.isEmpty() || updateValues == null) return false;
        if (updateValues.getId() != null)
            throw new IllegalArgumentException("record's id must be null");

        // check authority for update
        getContext().checkModelUpdate(getModelClass());

        // construct LambdaUpdateWrapper
        LambdaUpdateWrapper<T> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.setEntityClass(getModelClass());
        updateWrapper.in(T::getId, ids);

        // do update
        boolean res = SqlHelper.retBool(getBaseMapper().update(updateValues, updateWrapper));

        // process one2many fields
        processOne2ManyForUpdate(ids, updateValues);

        // process many2many fields
        processMany2ManyForUpdate(ids, updateValues);

        return res;
    }

    @SneakyThrows(IllegalAccessException.class)
    @SuppressWarnings("unchecked")
    private void processOne2ManyForUpdate(List<Long> ids, T record) {
        for (Field field : RelationFieldUtil.getOne2ManyFields(getModelClass())) {
            Class<BaseModel> targetClass = RelationFieldUtil.getTargetModelClass(getModelClass(), field);
            Field inverseField = RelationFieldUtil.getInverseField(getModelClass(), field);
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
        for (Field field : RelationFieldUtil.getMany2ManyFields(getModelClass())) {
            Class<? extends BaseModel> targetClass = RelationFieldUtil.getTargetModelClass(getModelClass(), field);
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
                            RelationMapper mapper = getRelationMapper(field);
                            mapper.add(sourceId, command.getIds());
                            break;
                        }
                        case REMOVE: {
                            RelationMapper mapper = getRelationMapper(field);
                            mapper.remove(sourceId, command.getIds());
                            break;
                        }
                        case REPLACE: {
                            RelationMapper mapper = getRelationMapper(field);
                            mapper.replace(sourceId, command.getIds());
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
    public boolean deleteById(Long id) {
        if (id == null) return false;
        return deleteByIds(List.of(id));
    }

    @Override
    public boolean deleteByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) return false;

        // check authority for delete
        getContext().checkModelDelete(getModelClass());

        // do delete
        boolean res = SqlHelper.retBool(getBaseMapper().deleteBatchIds(ids));

        // process one2many fields
        processOne2manyForDelete(ids);

        // process many2many fields
        processMany2manyForDelete(ids);
        return res;
    }

    private void processOne2manyForDelete(List<Long> ids) {
        for (Field field : RelationFieldUtil.getOne2ManyFields(getModelClass())) {
            Class<BaseModel> targetClass = RelationFieldUtil.getTargetModelClass(getModelClass(), field);
            Field inverseField = RelationFieldUtil.getInverseField(getModelClass(), field);
            AbstractBaseService<BaseModel> targetService = getService(targetClass);
            Many2OneField many2OneField = inverseField.getDeclaredAnnotation(Many2OneField.class);
            OnDeleteType onDeleteType = many2OneField == null ? OnDeleteType.RESTRICT : many2OneField.onDelete();
            switch (onDeleteType) {
                case RESTRICT: {
                    if (targetService.countByMany2OneIds(inverseField, ids) > 0) {
                        // TODO: optimize exception type
                        throw new RuntimeException("Records can't be deleted, because the " + field.getName() + " field is not empty.");
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
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        queryWrapper.in(toColumnName(field), ids);
        return selectCount(queryWrapper);
    }

    protected List<Long> selectIdsByMany2OneIds(Field field, List<Long> ids) {
        if (ids == null || ids.isEmpty()) return Collections.emptyList();
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        queryWrapper.in(toColumnName(field), ids);
        return getBaseMapper().selectList(queryWrapper).stream().map(BaseModel::getId).toList();
    }

    protected void updateMany2OneToNullByIds(Field field, List<Long> ids) {
        if (ids == null || ids.isEmpty()) return;
        UpdateWrapper<T> updateWrapper = new UpdateWrapper<>();
        updateWrapper.in("id", ids);
        updateWrapper.set(toColumnName(field), null);
        getBaseMapper().update(null, updateWrapper);
    }

    private String toColumnName(Field field) {
        return field.getName().replaceAll("(.)(\\p{Upper})", "$1_$2").toLowerCase();
    }

    private void processMany2manyForDelete(List<Long> ids) {
        for (Field field : RelationFieldUtil.getMany2ManyFields(getModelClass())) {
            for (Long sourceId : ids) {
                RelationMapper mapper = getRelationMapper(field);
                mapper.removeAll(sourceId);
            }
        }
    }

    @Override
    public Class<T> getModelClass() {
        // Double-checked locking for thread-safely lazy loading
        if (modelClass == null) {
            synchronized (modelClassLock) {
                if (modelClass == null) {
                    @SuppressWarnings("unchecked")
                    Class<T> clazz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
                    modelClass = clazz;
                }
            }
        }
        return modelClass;
    }

    @Override
    public Class<BaseMapper<T>> getBaseMapperClass() {
        // Double-checked locking for thread-safely lazy loading
        if (baseMapperClass == null) {
            synchronized (baseMapperClassLock) {
                if (baseMapperClass == null) {
                    @SuppressWarnings("unchecked")
                    Class<BaseMapper<T>> baseMapperClass1 = (Class<BaseMapper<T>>) getBaseMapper().getClass().getInterfaces()[0];
                    baseMapperClass = baseMapperClass1;
                }
            }
        }

        return baseMapperClass;
    }

    @Override
    public BaseMapper<T> getBaseMapper() {
        // Double-checked locking for thread-safely lazy loading
        if (baseMapper == null) {
            synchronized (baseMapperLock) {
                if (baseMapper == null) {
                    baseMapper = getContext().getBaseMapper(getModelClass());
                }
            }
        }
        return baseMapper;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <S extends BaseService<AT>, AT extends BaseModel> S getService(Class<AT> modelClass) {
        return (S) BaseServiceRegistry.getByModelClass(modelClass);
    }

    @Override
    public LambdaQueryChainWrapper<T> lambdaQuery() {
        getContext().checkModelSelect(getModelClass());
        return ChainWrappers.lambdaQueryChain(getBaseMapper(), getModelClass());
    }

    @PostConstruct
    public void afterPropertiesSet() {
        BaseServiceRegistry.register(this);
    }
}
