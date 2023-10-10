package com.hzhg.plm.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hzhg.plm.common.R;
import com.hzhg.plm.entity.Role;
import com.hzhg.plm.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;


@Tag(name = "角色接口")
@RestController
@RequestMapping("/role")
public class RoleController extends BaseController<RoleService, Role> {

    @Operation(summary = "分页查询")
    @GetMapping
    public R<IPage<Role>> page(
            @Nullable @RequestParam Long pageNum,
            @Nullable @RequestParam Long pageSize,
            @Nullable @RequestParam String name,
            @Nullable @RequestParam String code,
            @Nullable @RequestParam Integer status
    ) {
        IPage<Role> page = new Page<>(pageNum == null ? 1 : pageNum, pageSize == null ? 20 : pageSize);
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Role::getOrderNum);
        queryWrapper.like(name != null, Role::getName, name);
        queryWrapper.eq(status != null, Role::getStatus, status);
        return R.success(service.page(page, queryWrapper));
    }
}
