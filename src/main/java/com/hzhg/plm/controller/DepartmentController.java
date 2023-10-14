package com.hzhg.plm.controller;

import com.hzhg.plm.core.controller.BaseController;
import com.hzhg.plm.core.protocal.R;
import com.hzhg.plm.entity.Department;
import com.hzhg.plm.service.DepartmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "部门接口")
@RestController
@RequestMapping("/department")
public class DepartmentController extends BaseController<DepartmentService, Department> {

    @Operation(summary = "查询部门列表")
    @GetMapping
    public R<List<Department>> list() {
        return R.success(service.list());
    }
}
