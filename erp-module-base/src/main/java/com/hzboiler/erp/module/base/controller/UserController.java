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

    @Override
    public Result<Boolean> update(@RequestBody @NotEmpty(groups = UpdateValidationGroup.class) List<@Valid User> updateDtoList) {
        // SYS_ADMIN can update any user
        if (getContext().isAdmin()) {
            return super.update(updateDtoList);
        }

        // Current user can only update info of himself
        if (updateDtoList.size() != 1 || !Objects.equals(updateDtoList.get(0).getId(), getContext().getUserId())) {
            return Result.error(ERROR_ACCESS_DENIED);
        }

        return super.update(updateDtoList);
    }
}
