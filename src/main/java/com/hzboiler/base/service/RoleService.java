package com.hzboiler.base.service;

import com.hzboiler.base.model.Role;
import com.hzboiler.core.service.BaseService;

import java.util.Set;

public interface RoleService extends BaseService<Role> {

    Set<Role> getRolesByUserId(Long userId);

    Role getRoleByCode(String code);

    void addUserRoles(Long userId, Set<Long> roleIds);

    void removeUserRoles(Long userId, Set<Long> roleIds);

    void replaceUserRoles(Long userId, Set<Long> roleIds);
}
