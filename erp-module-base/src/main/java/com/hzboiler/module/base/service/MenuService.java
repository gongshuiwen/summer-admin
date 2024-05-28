package com.hzboiler.module.base.service;

import com.hzboiler.module.base.model.Menu;
import com.hzboiler.core.service.BaseService;

import java.util.List;

public interface MenuService extends BaseService<Menu> {

    List<Menu> getMenusTree();
}
