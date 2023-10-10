package com.hzhg.plm.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hzhg.plm.common.R;
import com.hzhg.plm.entity.base.BaseEntity;
import io.swagger.v3.oas.annotations.Operation;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;


public abstract class BaseController<S extends IService<T>, T extends BaseEntity> {

    @Autowired
    S service;

    @Operation(summary = "获取信息")
    @GetMapping("/{id}")
    public R<T> get(@PathVariable Long id) {
        return R.success(service.getById(id));
    }

    @Operation(summary = "创建信息")
    @PostMapping
    public R<Boolean> create(@RequestBody T roleDto) {
        return R.success(service.save(roleDto));
    }

    @Operation(summary = "更新信息")
    @PutMapping("/{id}")
    public R<Boolean> update(@PathVariable Long id, @RequestBody T roleDto) {
        roleDto.setId(id);
        return R.success(service.updateById(roleDto));
    }

    @Operation(summary = "删除信息")
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@PathVariable Long id) {
        return R.success(service.removeById(id));
    }

    @Operation(summary = "通用分页查询")
    @PostMapping("/page")
    public R<IPage<T>> page( @RequestBody Query<T> query ) {
        IPage<T> page = new Page<>( query.getPageNum(), query.getPageSize());
        QueryWrapper<T> queryWrapper = query.buildPageQueryWrapper(new QueryWrapper<>());
        return R.success(service.page(page, queryWrapper));
    }

    @Operation(summary = "通用计数查询")
    @PostMapping("/count")
    public R<Long> count( @RequestBody Query<T> query ) {
        QueryWrapper<T> queryWrapper = query.buildCountQueryWrapper(new QueryWrapper<>());
        return R.success(service.count(queryWrapper));
    }

    @Operation(summary = "批量创建")
    @PostMapping("/batch")
    public R<Boolean> createBatch( @RequestBody List<T> roleDtoList ) {
        return R.success(service.saveBatch(roleDtoList));
    }

    @Operation(summary = "批量更新")
    @PutMapping("/batch")
    public R<Boolean> updateBatch( @RequestParam List<Long> ids, @RequestBody T roleDto ) {
        LambdaUpdateWrapper<T> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(T::getId, ids);
        return R.success(service.update(roleDto, updateWrapper));
    }

    @Operation(summary = "批量删除")
    @DeleteMapping("/batch")
    public R<Boolean> deleteBatch( @RequestParam List<Long> ids ) {
        return R.success(service.removeBatchByIds(ids));
    }

    @Setter
    @Getter
    public static class Query<C> {
        Long pageSize = 20L;
        Long pageNum = 1L;
        List<Domain> domains;
        String order;

        public QueryWrapper<C> buildPageQueryWrapper(QueryWrapper<C> queryWrapper) {
            buildDomains(queryWrapper);
            buildSorts(queryWrapper);
            return queryWrapper;
        }

        public QueryWrapper<C> buildCountQueryWrapper(QueryWrapper<C> queryWrapper) {
            buildDomains(queryWrapper);
            return queryWrapper;
        }

        private void buildDomains(QueryWrapper<C> queryWrapper) {
            // TODO: optimize performance
            Class<QueryWrapper> clazz = QueryWrapper.class;
            for (Domain domain : domains) {
                String op = domain.getOperator();
                try {
                    Method method = clazz.getMethod(op, boolean.class, Object.class, Object.class);
                    method.invoke(queryWrapper, true, domain.getColumn(), domain.getValue());
                } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        private void buildSorts(QueryWrapper<C> queryWrapper) {
            if (getOrder() == null) {
                return ;
            }

            for (String s : getOrder().split(",")) {
                if (s.endsWith(" asc")) {
                    queryWrapper.orderByAsc(s.substring(0, s.length() - 4));
                } else if (s.endsWith(" desc")) {
                    queryWrapper.orderByDesc(s.substring(0, s.length() - 5));
                } else {
                    queryWrapper.orderByAsc(s);
                }
            }
        }
    }

    @Setter
    @Getter
    public static class Domain {
        String column;
        String operator;
        Object value;
    }
}
