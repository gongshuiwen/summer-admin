package com.hzhg.plm.controller;


import com.hzhg.plm.common.R;
import com.hzhg.plm.entity.Menu;
import com.hzhg.plm.service.MenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "菜单接口")
@RestController
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    MenuService menuService;

    @Operation(summary = "查询菜单树")
    @GetMapping
    public R<List<Menu>> tree() {
        return R.success(menuService.getMenusTree());
    }
}
