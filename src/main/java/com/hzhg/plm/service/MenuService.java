package com.hzhg.plm.service;

import com.hzhg.plm.core.service.BaseService;
import com.hzhg.plm.entity.Menu;

import java.util.List;

public interface MenuService extends BaseService<Menu> {

    List<Menu> getMenusTree();
}
