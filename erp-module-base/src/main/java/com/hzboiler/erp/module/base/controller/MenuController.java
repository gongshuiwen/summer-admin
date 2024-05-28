package com.hzboiler.erp.module.base.controller;

import com.hzboiler.erp.module.base.model.Menu;
import com.hzboiler.erp.core.controller.BaseController;
import com.hzboiler.erp.core.protocal.R;
import com.hzboiler.erp.module.base.service.MenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Tag(name = "菜单接口")
@RequestMapping("/menu")
public class MenuController extends BaseController<MenuService, Menu> {

    @Operation(summary = "查询菜单树")
    @GetMapping("/tree")
    public R<List<Menu>> tree() {
        return R.success(service.getMenusTree());
    }
}
