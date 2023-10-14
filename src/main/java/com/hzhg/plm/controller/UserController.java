package com.hzhg.plm.controller;


import com.hzhg.plm.core.controller.BaseController;
import com.hzhg.plm.entity.User;
import com.hzhg.plm.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "用户接口")
@RestController
@RequestMapping("/user")
public class UserController extends BaseController<UserService, User> {

}
