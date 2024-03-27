package com.hzhg.plm.core.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hzhg.plm.core.entity.BaseEntity;
import com.hzhg.plm.core.protocal.Condition;
import com.hzhg.plm.core.protocal.Query;
import com.hzhg.plm.core.protocal.R;
import com.hzhg.plm.core.service.IBaseService;
import com.hzhg.plm.core.validation.CreateValidationGroup;
import com.hzhg.plm.core.validation.UpdateValidationGroup;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.util.Collections;
import java.util.List;


@Validated
public abstract class BaseController<S extends IBaseService<T>, T extends BaseEntity> implements InitializingBean {

    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public S service;
    public Class<T> entityClass;
    public String entityName;

    @Operation(summary = "获取信息")
    @GetMapping("/{id}")
    public R<T> selectById(@PathVariable Long id) throws NoSuchFieldException, IllegalAccessException {
        T entity = service.selectById(id);
        BaseEntity.fetchNames(Collections.singletonList(entity));
        return R.success(entity);
    }

    @Operation(summary = "批量获取")
    @GetMapping("/batch")
    public R<List<T>> selectByIds(@RequestParam @NotEmpty List<Long> ids) throws NoSuchFieldException, IllegalAccessException {
        List<T> entity = service.selectByIds(ids);
        BaseEntity.fetchNames(entity);
        return R.success(entity);
    }

    @Operation(summary = "通用分页查询")
    @PostMapping("/page")
    public R<IPage<T>> page(@RequestBody Query<T> query) throws NoSuchFieldException, IllegalAccessException {
        IPage<T> page = service.page(query.getPageNum(), query.getPageSize(), query.getCondition(), query.getSort());
        BaseEntity.fetchNames(page.getRecords());
        return R.success(page);
    }

    @Operation(summary = "通用计数查询")
    @PostMapping("/count")
    public R<Long> count(@RequestBody Condition<T> condition) {
        return R.success(service.count(condition));
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
        this.entityClass = (Class<T>) ((ParameterizedTypeImpl) this.getClass().getGenericSuperclass()).getActualTypeArguments()[1];
        this.entityName = entityClass.getSimpleName();
    }
}
