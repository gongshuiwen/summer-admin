package com.hzhg.plm.controller;

import com.hzhg.plm.core.controller.BaseController;
import com.hzhg.plm.entity.Department;
import com.hzhg.plm.service.DepartmentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Tag(name = "部门接口")
@RequestMapping("/department")
public class DepartmentController extends BaseController<DepartmentService, Department> {
}
