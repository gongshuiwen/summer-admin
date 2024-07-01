package com.hzboiler.erp.module.base.service;

import com.hzboiler.erp.core.service.BaseService;
import com.hzboiler.erp.module.base.model.Menu;

import java.util.List;

public interface MenuService extends BaseService<Menu> {

    List<Menu> getMenusTree();
}
