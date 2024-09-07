package io.summernova.admin.module.base.service;

import io.summernova.admin.core.service.BaseService;
import io.summernova.admin.module.base.model.Menu;

import java.util.List;

/**
 * @author gongshuiwen
 */
public interface MenuService extends BaseService<Menu> {

    List<Menu> getMenusTree();
}
