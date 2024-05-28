package com.hzboiler.erp.module.base.controller;

import com.hzboiler.erp.module.base.model.Department;
import com.hzboiler.erp.core.controller.BaseController;
import com.hzboiler.erp.module.base.service.DepartmentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "部门接口")
@RequestMapping("/department")
public class DepartmentController extends BaseController<DepartmentService, Department> {
}
