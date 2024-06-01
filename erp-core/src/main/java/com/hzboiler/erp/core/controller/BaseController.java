package com.hzboiler.erp.core.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hzboiler.erp.core.context.BaseContext;
import com.hzboiler.erp.core.context.BaseContextHolder;
import com.hzboiler.erp.core.exception.BusinessException;
import com.hzboiler.erp.core.field.util.RelationFieldUtil;
import com.hzboiler.erp.core.field.util.ReadOnlyUtil;
import com.hzboiler.erp.core.protocal.query.Condition;
import com.hzboiler.erp.core.validation.CreateValidationGroup;
import com.hzboiler.erp.core.model.BaseModel;
import com.hzboiler.erp.core.field.Many2Many;
import com.hzboiler.erp.core.field.Many2One;
import com.hzboiler.erp.core.mapper.RelationMapper;
import com.hzboiler.erp.core.mapper.RelationMapperRegistry;
import com.hzboiler.erp.core.protocal.Result;
import com.hzboiler.erp.core.service.BaseService;
import com.hzboiler.erp.core.validation.UpdateValidationGroup;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.stream.Collectors;

import static com.hzboiler.erp.core.exception.CoreBusinessExceptionEnums.ERROR_INVALID_ARGUMENTS;

@Validated
public abstract class BaseController<S extends BaseService<T>, T extends BaseModel> implements InitializingBean {

    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    protected S service;
    protected Class<T> entityClass;

    @Operation(summary = "ID查询")
    @GetMapping
    public Result<List<T>> select(@RequestParam @NotEmpty List<Long> ids) throws IllegalAccessException {
        List<T> records = service.selectByIds(ids);
        if (records != null && !records.isEmpty()) {
            fetchMany2One(records);
            fetchMany2Many(records);
        }
        return Result.success(records);
    }

    @Operation(summary = "分页查询")
    @GetMapping("/page")
    public Result<IPage<T>> page(@RequestParam @Positive @Max(1000) Long pageNum, @RequestParam @Positive @Max(1000) Long pageSize,
                                 @RequestParam(required = false) String sorts,
                                 @RequestBody(required = false) Condition condition) throws IllegalAccessException {
        IPage<T> page = service.page(pageNum, pageSize, sorts, condition);
        List<T> records = page.getRecords();
        if (records != null && !records.isEmpty()) {
            fetchMany2One(records);
            fetchMany2Many(records);
        }
        return Result.success(page);
    }

    @Operation(summary = "计数查询")
    @GetMapping("/count")
    public Result<Long> count(@RequestBody(required = false) Condition condition) {
        return Result.success(service.count(condition));
    }

    @Operation(summary = "名称查询")
    @GetMapping("/nameSearch")
    public Result<List<T>> nameSearch(@RequestParam @NotBlank String name) {
        List<T> records = service.nameSearch(name);
        return Result.success(records);
    }

    @Operation(summary = "创建记录")
    @PostMapping
    @Validated(CreateValidationGroup.class)
    public Result<List<T>> create(@RequestBody @NotEmpty(groups = CreateValidationGroup.class) List<@Valid T> createDtoList) {
        checkReadOnlyForDtoList(createDtoList);
        service.createBatch(createDtoList);
        return Result.success(createDtoList);
    }

    @Operation(summary = "更新记录")
    @PutMapping
    @Validated(UpdateValidationGroup.class)
    public Result<Boolean> update(@RequestBody @NotEmpty(groups = UpdateValidationGroup.class) List<@Valid T> updateDtoList) {
        checkReadOnlyForDtoList(updateDtoList);
        for (T t : updateDtoList) {
            service.updateById(t.getId(), t);
        }
        return Result.success(true);
    }

    @Operation(summary = "删除记录")
    @DeleteMapping
    public Result<Boolean> delete(@RequestParam @NotEmpty List<Long> ids) {
        return Result.success(service.deleteByIds(ids));
    }

    protected BaseContext getContext() {
        return BaseContextHolder.getContext();
    }

    @SuppressWarnings("unchecked")
    public void afterPropertiesSet() {
        this.entityClass = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[1];
    }

    private void fetchMany2One(List<T> entities) throws IllegalAccessException {
        for (Field field : RelationFieldUtil.getMany2OneFields(entityClass)) {
            // Get target ids
            Set<Long> targetIds = new HashSet<>();
            for (T entity : entities) {
                Long targetId = ((Many2One<?>) field.get(entity)).getId();
                if (targetId != null && targetId > 0) {
                    targetIds.add(((Many2One<?>) field.get(entity)).getId());
                }
            }

            // Get target entities
            BaseService<?> targetService = service.getService(RelationFieldUtil.getTargetModelClass(field));
            List<? extends BaseModel> targetEntities = targetService.selectByIds(targetIds.stream().toList());
            Map<Long, ? extends BaseModel> targetEntitiesMap = targetEntities.stream()
                    .collect(Collectors.toMap(BaseModel::getId, t -> t));

            // Set target entities to many2one field
            for (T entity : entities) {
                @SuppressWarnings("unchecked")
                Many2One<BaseModel> many2One = (Many2One<BaseModel>) field.get(entity);
                Long targetId = many2One.getId();
                if (targetId != null && targetId > 0) {
                    many2One.set(targetEntitiesMap.get(targetId));
                }
            }
        }
    }

    private void fetchMany2Many(List<T> entities) throws IllegalAccessException {
        for (Field field : RelationFieldUtil.getMany2ManyFields(entityClass)) {
            Class<? extends BaseModel> targetClass = RelationFieldUtil.getTargetModelClass(field);
            RelationMapper relationMapper = RelationMapperRegistry.getMapper(entityClass, targetClass);

            // Get all target ids and the map of entity id -> target ids
            Set<Long> allTargetIds = new HashSet<>();
            Map<Long, List<Long>> entityId2TargetIdsMap = new HashMap<>();
            for (T entity : entities) {
                List<Long> targetIds = relationMapper.getTargetIds(entityClass, entity.getId());
                allTargetIds.addAll(targetIds);
                entityId2TargetIdsMap.put(entity.getId(), targetIds);
            }

            // Get allTargetEntities
            BaseService<?> targetService = service.getService(targetClass);
            List<? extends BaseModel> allTargetEntities = targetService.selectByIds(allTargetIds.stream().toList());
            Map<Long, ? extends BaseModel> targetEntitiesMap = allTargetEntities.stream()
                    .collect(Collectors.toMap(BaseModel::getId, t -> t));

            // Set many2many field
            for (T entity : entities) {
                List<Long> targetIds = entityId2TargetIdsMap.get(entity.getId());
                List<? extends BaseModel> targetEntities = targetIds.stream()
                        .map(targetEntitiesMap::get).collect(Collectors.toList());
                field.set(entity, Many2Many.ofValues(targetEntities));
            }
        }
    }

    private void checkReadOnlyForDtoList(List<T> dtoList) {
        for (Field field : ReadOnlyUtil.getReadOnlyFields(entityClass)) {
            for (T dto : dtoList) {
                try {
                    if (field.get(dto) != null) {
                        throw new BusinessException(ERROR_INVALID_ARGUMENTS);
                    }
                } catch (IllegalAccessException e) {
                    // this should never happen
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
