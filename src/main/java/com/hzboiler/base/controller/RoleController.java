package com.hzboiler.base.controller;

import com.hzboiler.base.entity.Role;
import com.hzboiler.core.controller.BaseController;
import com.hzboiler.base.service.RoleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Tag(name = "角色接口")
@RequestMapping("/role")
public class RoleController extends BaseController<RoleService, Role> {
}
