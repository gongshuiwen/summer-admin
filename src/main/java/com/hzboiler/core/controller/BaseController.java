package com.hzboiler.core.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hzboiler.core.validation.CreateValidationGroup;
import com.hzboiler.core.entity.BaseEntity;
import com.hzboiler.core.fields.Many2Many;
import com.hzboiler.core.fields.Many2One;
import com.hzboiler.core.mapper.RelationMapper;
import com.hzboiler.core.mapper.RelationMapperRegistry;
import com.hzboiler.core.protocal.Condition;
import com.hzboiler.core.protocal.Query;
import com.hzboiler.core.protocal.R;
import com.hzboiler.core.service.BaseService;
import com.hzboiler.core.service.BaseServiceRegistry;
import com.hzboiler.core.validation.UpdateValidationGroup;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.stream.Collectors;


@Validated
public abstract class BaseController<S extends BaseService<T>, T extends BaseEntity> implements InitializingBean {

    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public S service;
    public Class<T> entityClass;
    public String entityName;

    @Operation(summary = "获取信息")
    @GetMapping("/{id}")
    public R<T> selectById(@PathVariable Long id) throws NoSuchFieldException, IllegalAccessException {
        T entity = service.selectById(id);
        if (entity != null) {
            fetchMany2One(Collections.singletonList(entity));
            fetchMany2Many(Collections.singletonList(entity));
        }
        return R.success(entity);
    }

    @Operation(summary = "批量获取")
    @GetMapping("/batch")
    public R<List<T>> selectByIds(@RequestParam @NotEmpty List<Long> ids) throws IllegalAccessException {
        List<T> entities = service.selectByIds(ids);
        fetchMany2One(entities);
        fetchMany2Many(entities);
        return R.success(entities);
    }

    @Operation(summary = "通用分页查询")
    @PostMapping("/page")
    public R<IPage<T>> page(@RequestBody Query<T> query) throws IllegalAccessException {
        IPage<T> page = service.page(query.getPageNum(), query.getPageSize(), query.getCondition(), query.getSort());
        fetchMany2One(page.getRecords());
        fetchMany2Many(page.getRecords());
        return R.success(page);
    }

    @Operation(summary = "通用计数查询")
    @PostMapping("/count")
    public R<Long> count(@RequestBody Condition<T> condition) {
        return R.success(service.count(condition));
    }

    @Operation(summary = "通用名称查询")
    @GetMapping("/nameSearch")
    public R<List<T>> nameSearch(@RequestParam String name) {
        List<T> entity = service.nameSearch(name);
        return R.success(entity);
    }

    @Operation(summary = "创建信息")
    @PostMapping
    @Validated(CreateValidationGroup.class)
    public R<T> createOne(@RequestBody @Valid T entityDto) {
        service.createOne(entityDto);
        return R.success(entityDto);
    }

    @Operation(summary = "批量创建")
    @PostMapping("/batch")
    @Validated(CreateValidationGroup.class)
    public R<List<T>> createBatch(@RequestBody @NotEmpty(groups = CreateValidationGroup.class) List<@Valid T> entityDtoList) {
        service.createBatch(entityDtoList);
        return R.success(entityDtoList);
    }

    @Operation(summary = "更新信息")
    @PutMapping("/{id}")
    @Validated(UpdateValidationGroup.class)
    public R<Boolean> updateById(@PathVariable Long id, @RequestBody @Valid T entityDto) {
        return R.success(service.updateById(id, entityDto));
    }

    @Operation(summary = "批量更新")
    @PutMapping("/batch")
    @Validated(UpdateValidationGroup.class)
    public R<Boolean> updateByIds(@RequestParam @NotEmpty(groups = UpdateValidationGroup.class) List<Long> ids, @RequestBody @Valid T entityDto) {
        return R.success(service.updateByIds(ids, entityDto));
    }

    @Operation(summary = "删除信息")
    @DeleteMapping("/{id}")
    public R<Boolean> deleteById(@PathVariable Long id) {
        return R.success(service.deleteById(id));
    }

    @Operation(summary = "批量删除")
    @DeleteMapping("/batch")
    public R<Boolean> deleteByIds(@RequestParam @NotEmpty List<Long> ids) {
        return R.success(service.deleteByIds(ids));
    }

    @SuppressWarnings("unchecked")
    public void afterPropertiesSet() {
        this.entityClass = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[1];
        this.entityName = entityClass.getSimpleName();
    }

    @SuppressWarnings("unchecked")
    private void fetchMany2One(List<T> entities) throws IllegalAccessException {
        // Do noting if empty
        if (entities == null || entities.isEmpty()) return;

        // Get entity class
        Class<?> entityClass = entities.get(0).getClass();
        for (Field field : Many2One.getMany2OneFields(entityClass)) {
            // Get target ids
            Set<Long> targetIds = new HashSet<>();
            for (T entity : entities) {
                Long targetId = ((Many2One<?>) field.get(entity)).getId();
                if (targetId != null && targetId > 0) {
                    targetIds.add(((Many2One<?>) field.get(entity)).getId());
                }
            }

            // Get target entities
            BaseService<T> targetService = BaseServiceRegistry.getService((Class<T>) Many2One.getTargetClass(field));
            List<? extends BaseEntity> targetEntities = targetService.selectByIds(targetIds.stream().toList());
            Map<Long, ? extends BaseEntity> targetEntitiesMap = targetEntities.stream()
                    .collect(Collectors.toMap(BaseEntity::getId, t -> t));

            // Set target entities to many2one field
            for (T entity : entities) {
                Many2One<BaseEntity> many2One = (Many2One<BaseEntity>) field.get(entity);
                Long targetId = many2One.getId();
                if (targetId != null && targetId > 0) {
                    many2One.set(targetEntitiesMap.get(targetId));
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void fetchMany2Many(List<T> entities) throws IllegalAccessException {
        // Do noting if empty
        if (entities == null || entities.isEmpty()) return;

        // Get entity class
        Class<?> entityClass = entities.get(0).getClass();
        for (Field field : Many2Many.getMany2ManyFields(entityClass)) {
            Class<?> targetClass = Many2Many.getTargetClass(field);
            RelationMapper relationMapper = RelationMapperRegistry.getMapper(entityClass, targetClass);

            // Get all target ids and the map of entity id -> target ids
            Set<Long> allTargetIds = new HashSet<>();
            Map<Long, List<Long>> entityId2TargetIdsMap = new HashMap<>();
            for (T entity : entities) {
                List<Long> targetIds = relationMapper.getTargetIds(entityClass, List.of(entity.getId()));
                allTargetIds.addAll(targetIds);
                entityId2TargetIdsMap.put(entity.getId(), targetIds);
            }

            // Get allTargetEntities
            BaseService<T> targetService = BaseServiceRegistry.getService((Class<T>) targetClass);
            List<? extends BaseEntity> allTargetEntities = targetService.selectByIds(allTargetIds.stream().toList());
            Map<Long, ? extends BaseEntity> targetEntitiesMap = allTargetEntities.stream()
                    .collect(Collectors.toMap(BaseEntity::getId, t -> t));

            // Set many2many field
            for (T entity : entities) {
                List<Long> targetIds = entityId2TargetIdsMap.get(entity.getId());
                List<? extends BaseEntity> targetEntities = targetIds.stream()
                        .map(targetEntitiesMap::get).collect(Collectors.toList());
                field.set(entity, Many2Many.ofValues(targetEntities));
            }
        }
    }
}
