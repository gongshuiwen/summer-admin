package com.hzhg.plm.controller;

import com.hzhg.plm.common.R;
import com.hzhg.plm.entity.Department;
import com.hzhg.plm.service.DepartmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "部门接口")
@RestController
@RequestMapping("/department")
public class DepartmentController {

    @Autowired
    DepartmentService departmentService;

    @Operation(summary = "查询部门列表")
    @GetMapping
    public R<List<Department>> list() {
        return R.success(departmentService.list());
    }

    @Operation(summary = "获取部门信息")
    @GetMapping("/{id}")
    public R<Department> get(@PathVariable Long id) {
        return R.success(departmentService.getById(id));
    }

    @Operation(summary = "创建部门信息")
    @PostMapping
    public R<Boolean> create(@RequestBody Department departmentDto) {
        return R.success(departmentService.save(departmentDto));
    }

    @Operation(summary = "更新部门信息")
    @PutMapping("/{id}")
    public R<Boolean> update(@PathVariable Long id, @RequestBody Department departmentDto) {
        departmentDto.setId(id);
        return R.success(departmentService.updateById(departmentDto));
    }

    @Operation(summary = "删除部门信息")
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@PathVariable Long id) {
        return R.success(departmentService.removeById(id));
    }
}
