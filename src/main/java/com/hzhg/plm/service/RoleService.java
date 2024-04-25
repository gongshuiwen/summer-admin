package com.hzhg.plm.service;

import com.hzhg.plm.core.service.BaseService;
import com.hzhg.plm.entity.Role;

import java.util.Set;

public interface RoleService extends BaseService<Role> {

    Set<Role> getRolesByUserId(Long userId);

    Role getRoleByCode(String code);

    void addUserRoles(Long userId, Set<Long> roleIds);

    void removeUserRoles(Long userId, Set<Long> roleIds);

    void replaceUserRoles(Long userId, Set<Long> roleIds);
}
