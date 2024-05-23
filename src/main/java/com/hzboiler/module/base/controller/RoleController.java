package com.hzboiler.module.base.controller;

import com.hzboiler.module.base.model.Role;
import com.hzboiler.core.controller.BaseController;
import com.hzboiler.module.base.service.RoleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Tag(name = "角色接口")
@RequestMapping("/role")
public class RoleController extends BaseController<RoleService, Role> {
}
