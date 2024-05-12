package com.hzboiler.base.controller;


import com.hzboiler.base.entity.User;
import com.hzboiler.core.controller.BaseController;
import com.hzboiler.core.protocal.R;
import com.hzboiler.base.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import static com.hzboiler.core.utils.Constants.ROLE_SYS_ADMIN;

@RestController
@Tag(name = "用户接口")
@RequestMapping("/user")
public class UserController extends BaseController<UserService, User> {

    public static final String EXPRESSION_AUTHORITY_USER =
            "hasRole('" + ROLE_SYS_ADMIN + "') or #id == authentication.principal.id";

    @Override
    @PreAuthorize(EXPRESSION_AUTHORITY_USER)
    public R<Boolean> updateById(@PathVariable Long id, @Valid @RequestBody User roleDto) {
        return super.updateById(id, roleDto);
    }
}
