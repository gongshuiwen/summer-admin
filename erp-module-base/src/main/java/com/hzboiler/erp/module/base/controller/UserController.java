package com.hzboiler.erp.module.base.controller;

import com.hzboiler.erp.core.controller.BaseController;
import com.hzboiler.erp.module.base.model.User;
import com.hzboiler.erp.module.base.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "用户接口")
@RequestMapping("/user")
public class UserController extends BaseController<UserService, User> {
}
