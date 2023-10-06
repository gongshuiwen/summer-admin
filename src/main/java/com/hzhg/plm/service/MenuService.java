package com.hzhg.plm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hzhg.plm.entity.Menu;

import java.util.List;

public interface MenuService extends IService<Menu> {

    List<Menu> getMenusTree();
}
