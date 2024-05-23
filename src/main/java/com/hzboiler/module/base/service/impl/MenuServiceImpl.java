package com.hzboiler.module.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hzboiler.module.base.model.Menu;
import com.hzboiler.core.service.AbstractBaseService;
import com.hzboiler.module.base.mapper.MenuMapper;
import com.hzboiler.module.base.service.MenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.hzboiler.core.entity.TreeBaseEntity.buildTree;

@Slf4j
@Service
public class MenuServiceImpl extends AbstractBaseService<MenuMapper, Menu> implements MenuService {

    public List<Menu> getMenusTree() {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Menu::getOrderNum);
        return buildTree(list(queryWrapper));
    }
}
