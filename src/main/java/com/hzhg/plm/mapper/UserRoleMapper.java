package com.hzhg.plm.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.Set;

@Mapper
public interface UserRoleMapper {

    default Set<Long> getRoleIdsByUserId(Long userId) {
        return getRoleIdsByUserIds(Set.of(userId));
    };

    Set<Long> getRoleIdsByUserIds(Set<Long> userIds);

    default Set<Long> getUserIdsByRoleId(Long roleId) {
        return getUserIdsByRoleIds(Set.of(roleId));
    };

    Set<Long> getUserIdsByRoleIds(Set<Long> roleIds);

    void addUserRoles(Long userId, Set<Long> roleIds);

    void removeUserRoles(Long userId, Set<Long> roleIds);

    void removeUserRolesAll(Long userId);
}
