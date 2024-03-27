package com.hzhg.plm.service;

import com.hzhg.plm.core.service.IBaseService;
import com.hzhg.plm.entity.Menu;

import java.util.List;

public interface MenuService extends IBaseService<Menu> {

    List<Menu> getMenusTree();
}
