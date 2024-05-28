package com.hzboiler.erp.module.base.controller;

import com.hzboiler.erp.module.base.model.User;
import com.hzboiler.erp.core.controller.BaseController;
import com.hzboiler.erp.core.protocal.R;
import com.hzboiler.erp.module.base.service.UserService;
import com.hzboiler.erp.core.validation.UpdateValidationGroup;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;
import java.util.Objects;

import static com.hzboiler.erp.core.exception.CoreBusinessExceptionEnums.ERROR_ACCESS_DENIED;

@Tag(name = "用户接口")
@RestController
@RequestMapping("/user")
public class UserController extends BaseController<UserService, User> {

    @Override
    public R<Boolean> update(@RequestBody @NotEmpty(groups = UpdateValidationGroup.class) List<@Valid User> updateDtoList) {
        // SYS_ADMIN can update any user
        if (getContext().isAdmin()) {
            return super.update(updateDtoList);
        }

        // Current user can only update info of himself
        if (updateDtoList.size() != 1 || !Objects.equals(updateDtoList.get(0).getId(), getContext().getUserId())) {
            return R.error(ERROR_ACCESS_DENIED);
        }

        return super.update(updateDtoList);
    }
}
