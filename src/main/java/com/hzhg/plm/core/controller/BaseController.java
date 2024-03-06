package com.hzhg.plm.core.controller;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hzhg.plm.core.entity.BaseEntity;
import com.hzhg.plm.core.protocal.Query;
import com.hzhg.plm.core.protocal.R;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.util.Collections;
import java.util.List;


public abstract class BaseController<S extends IService<T>, T extends BaseEntity> implements InitializingBean {

    @Autowired
    public S service;

    public Class<T> entityClass;

    public String entityName;

    public static final String ROLE_ADMIN = "SYS_ADMIN";
    public static final String AUTHORITY_SELECT = "SELECT";
    public static final String AUTHORITY_CREATE = "CREATE";
    public static final String AUTHORITY_UPDATE = "UPDATE";
    public static final String AUTHORITY_DELETE = "DELETE";
    public static final String AUTHORITY_DELIMITER = ":";
    public static final String EXPRESSION_PREFIX = "hasRole('" + ROLE_ADMIN + "') or hasAuthority(#root.getThis().entityName + '" + AUTHORITY_DELIMITER;
    public static final String EXPRESSION_SUFFIX = "')";

    public static final String EXPRESSION_AUTHORITY_SELECT = EXPRESSION_PREFIX + AUTHORITY_SELECT + EXPRESSION_SUFFIX;
    public static final String EXPRESSION_AUTHORITY_CREATE = EXPRESSION_PREFIX + AUTHORITY_CREATE + EXPRESSION_SUFFIX;
    public static final String EXPRESSION_AUTHORITY_UPDATE = EXPRESSION_PREFIX + AUTHORITY_UPDATE + EXPRESSION_SUFFIX;
    public static final String EXPRESSION_AUTHORITY_DELETE = EXPRESSION_PREFIX + AUTHORITY_DELETE + EXPRESSION_SUFFIX;

    @Operation(summary = "获取信息")
    @GetMapping("/{id}")
    @PreAuthorize(value = EXPRESSION_AUTHORITY_SELECT)
    public R<T> get(@PathVariable Long id) throws NoSuchFieldException, IllegalAccessException {
        T entity = service.getById(id);
        BaseEntity.fetchNames(Collections.singletonList(entity));
        return R.success(entity);
    }

    @Operation(summary = "创建信息")
    @PostMapping
    @PreAuthorize(value = EXPRESSION_AUTHORITY_CREATE)
    public R<T> create(@RequestBody T roleDto) {
        service.save(roleDto);
        return R.success(roleDto);
    }

    @Operation(summary = "更新信息")
    @PutMapping("/{id}")
    @PreAuthorize(value = EXPRESSION_AUTHORITY_UPDATE)
    public R<Boolean> update(@PathVariable Long id, @RequestBody T roleDto) {
        roleDto.setId(id);
        return R.success(service.updateById(roleDto));
    }

    @Operation(summary = "删除信息")
    @DeleteMapping("/{id}")
    @PreAuthorize(value = EXPRESSION_AUTHORITY_DELETE)
    public R<Boolean> delete(@PathVariable Long id) {
        return R.success(service.removeById(id));
    }

    @Operation(summary = "批量创建")
    @PostMapping("/batch")
    @PreAuthorize(value = EXPRESSION_AUTHORITY_CREATE)
    public R<List<T>> batchCreate( @RequestBody List<T> roleDtoList ) {
        service.saveBatch(roleDtoList);
        return R.success(roleDtoList);
    }

    @Operation(summary = "批量更新")
    @PutMapping("/batch")
    @PreAuthorize(value = EXPRESSION_AUTHORITY_UPDATE)
    public R<Boolean> batchUpdate( @RequestParam List<Long> ids, @RequestBody T roleDto ) {
        LambdaUpdateWrapper<T> updateWrapper = new LambdaUpdateWrapper<>(entityClass);
        updateWrapper.in(T::getId, ids);
        return R.success(service.update(roleDto, updateWrapper));
    }

    @Operation(summary = "批量删除")
    @DeleteMapping("/batch")
    @PreAuthorize(value = EXPRESSION_AUTHORITY_DELETE)
    public R<Boolean> batchDelete( @RequestParam List<Long> ids ) {
        return R.success(service.removeBatchByIds(ids));
    }

    @Operation(summary = "通用分页查询")
    @PostMapping("/page")
    @PreAuthorize(value = EXPRESSION_AUTHORITY_SELECT)
    public R<IPage<T>> page( @RequestBody Query<T> query ) throws NoSuchFieldException, IllegalAccessException {
        IPage<T> page = new Page<>( query.getPageNum(), query.getPageSize());
        page = service.page(page, query.buildPageQueryWrapper());
        BaseEntity.fetchNames(page.getRecords());
        return R.success(page);
    }

    @Operation(summary = "通用计数查询")
    @PostMapping("/count")
    @PreAuthorize(value = EXPRESSION_AUTHORITY_SELECT)
    public R<Long> count( @RequestBody Query<T> query ) {
        return R.success(service.count(query.buildCountQueryWrapper()));
    }

    public void afterPropertiesSet() {
        this.entityClass = (Class<T>) ((ParameterizedTypeImpl) this.getClass().getGenericSuperclass()).getActualTypeArguments()[1];
        this.entityName = entityClass.getSimpleName();
    };
}
