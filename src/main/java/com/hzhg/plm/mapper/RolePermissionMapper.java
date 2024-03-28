package com.hzhg.plm.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.Set;

@Mapper
public interface RolePermissionMapper {

    default Set<Long> getPermissionIdsByRoleId(Long roleId) {
        return getPermissionIdsByRoleIds(Set.of(roleId));
    };

    Set<Long> getPermissionIdsByRoleIds(Set<Long> roleIds);

    default Set<Long> getRoleIdsByPermissionId(Long permId) {
        return getRoleIdsByPermissionIds(Set.of(permId));
    };

    Set<Long> getRoleIdsByPermissionIds(Set<Long> permIds);

    void addRolePermissions(Long roleId, Set<Long> permIds);

    void removeRolePermissions(Long roleId, Set<Long> permIds);

    void removeRolePermissionsAll(Long roleId);
}
