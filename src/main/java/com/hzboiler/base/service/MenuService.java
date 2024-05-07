package com.hzboiler.base.service;

import com.hzboiler.base.model.Menu;
import com.hzboiler.core.service.BaseService;

import java.util.List;

public interface MenuService extends BaseService<Menu> {

    List<Menu> getMenusTree();
}
