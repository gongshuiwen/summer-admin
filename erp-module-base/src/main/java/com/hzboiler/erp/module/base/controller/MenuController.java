package com.hzboiler.erp.module.base.controller;

import com.hzboiler.erp.core.controller.BaseController;
import com.hzboiler.erp.module.base.model.Menu;
import com.hzboiler.erp.module.base.service.MenuService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "菜单接口")
@RequestMapping("/menu")
public class MenuController extends BaseController<MenuService, Menu> {
}
