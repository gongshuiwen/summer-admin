package com.hzhg.plm.mapper;

import com.hzhg.plm.core.mapper.MapperRelation;
import com.hzhg.plm.core.mapper.RelationMapper;
import com.hzhg.plm.entity.Permission;
import com.hzhg.plm.entity.Role;
import org.apache.ibatis.annotations.Mapper;

import java.util.Set;

@Mapper
@MapperRelation(table = "role_permission",
        field1 = "role_id", class1 = Role.class,
        field2 = "perm_id", class2 = Permission.class)
public interface RolePermissionMapper extends RelationMapper {

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
