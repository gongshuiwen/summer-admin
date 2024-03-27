package com.hzhg.plm.controller;

import com.hzhg.plm.core.controller.BaseController;
import com.hzhg.plm.entity.Role;
import com.hzhg.plm.service.RoleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Tag(name = "角色接口")
@RequestMapping("/role")
public class RoleController extends BaseController<RoleService, Role> {
}
