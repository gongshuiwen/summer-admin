package com.hzhg.plm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hzhg.plm.core.service.AbstractBaseService;
import com.hzhg.plm.entity.Menu;
import com.hzhg.plm.mapper.MenuMapper;
import com.hzhg.plm.service.MenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.hzhg.plm.core.entity.TreeBaseEntity.buildTree;

@Slf4j
@Service
public class MenuServiceImpl extends AbstractBaseService<MenuMapper, Menu> implements MenuService {

    public List<Menu> getMenusTree() {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Menu::getOrderNum);
        return buildTree(list(queryWrapper));
    }
}
