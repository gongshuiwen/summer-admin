package com.hzboiler.erp.module.base.service.impl;

import com.hzboiler.erp.core.method.annotations.Public;
import com.hzboiler.erp.core.service.AbstractBaseTreeService;
import com.hzboiler.erp.module.base.model.Menu;
import com.hzboiler.erp.module.base.mapper.MenuMapper;
import com.hzboiler.erp.module.base.service.MenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class MenuServiceImpl extends AbstractBaseTreeService<MenuMapper, Menu> implements MenuService {

    @Public
    public List<Menu> getMenusTree() {
        return buildTree(lambdaQuery().orderByAsc(Menu::getOrderNum).list());
    }
}
