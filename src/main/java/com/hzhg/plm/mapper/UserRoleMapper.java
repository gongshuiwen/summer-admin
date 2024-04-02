package com.hzhg.plm.mapper;

import com.hzhg.plm.core.mapper.MapperRelation;
import com.hzhg.plm.core.mapper.RelationMapper;
import com.hzhg.plm.entity.Role;
import com.hzhg.plm.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.Set;

@Mapper
@MapperRelation(table = "user_role",
        field1 = "user_id", class1 = User.class,
        field2 = "role_id", class2 = Role.class)
public interface UserRoleMapper extends RelationMapper {

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
