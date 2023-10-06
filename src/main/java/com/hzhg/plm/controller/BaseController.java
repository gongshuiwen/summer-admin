package com.hzhg.plm.controller;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hzhg.plm.common.R;
import com.hzhg.plm.entity.base.BaseEntity;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
}
