package com.hzboiler.base.service;

import com.hzboiler.base.entity.Permission;
import com.hzboiler.core.service.BaseService;

import java.util.Set;

public interface PermissionService extends BaseService<Permission> {

    Set<Permission> getPermissionsByRoleId(Long roleId);

    Set<Permission> getPermissionsByRoleIds(Set<Long> roleIds);

    void addRolePermissions(Long roleId, Set<Long> permIds);

    void removeRolePermissions(Long roleId, Set<Long> permIds);

    void replaceRolePermissions(Long roleId, Set<Long> permIds);
}
