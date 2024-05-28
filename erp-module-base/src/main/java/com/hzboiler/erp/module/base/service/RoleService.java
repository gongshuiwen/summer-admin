package com.hzboiler.erp.module.base.service;

import com.hzboiler.erp.module.base.model.Role;
import com.hzboiler.erp.core.service.BaseService;

import java.util.Set;

public interface RoleService extends BaseService<Role> {

    Set<Role> getRolesByUserId(Long userId);

    Role getRoleByCode(String code);

    void addUserRoles(Long userId, Set<Long> roleIds);

    void removeUserRoles(Long userId, Set<Long> roleIds);

    void replaceUserRoles(Long userId, Set<Long> roleIds);
}
