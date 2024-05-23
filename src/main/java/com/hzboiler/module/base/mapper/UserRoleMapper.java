package com.hzboiler.module.base.mapper;

import com.hzboiler.module.base.model.Role;
import com.hzboiler.module.base.model.User;
import com.hzboiler.core.mapper.MapperRelation;
import com.hzboiler.core.mapper.RelationMapper;
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
