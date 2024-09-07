package io.summernova.admin.module.base.service;

import io.summernova.admin.core.method.annotations.Public;
import io.summernova.admin.core.service.AbstractBaseTreeService;
import io.summernova.admin.module.base.model.Menu;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author gongshuiwen
 */
@Slf4j
@Service
public class MenuServiceImpl extends AbstractBaseTreeService<Menu> implements MenuService {

    @Public
    @Override
    public List<Menu> getMenusTree() {
        return buildTree(lambdaQuery().orderByAsc(Menu::getOrderNum).list());
    }
}
