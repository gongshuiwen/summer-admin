package com.hzhg.plm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hzhg.plm.entity.Permission;

import java.util.Set;

public interface PermissionService extends IService<Permission> {

    Set<Permission> getPermissionsByRoleId(Long roleId);

    Set<Permission> getPermissionsByRoleIds(Set<Long> roleIds);

    void addRolePermissions(Long roleId, Set<Long> permIds);

    void removeRolePermissions(Long roleId, Set<Long> permIds);

    void replaceRolePermissions(Long roleId, Set<Long> permIds);
}
