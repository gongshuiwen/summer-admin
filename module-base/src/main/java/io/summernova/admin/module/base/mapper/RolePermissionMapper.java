package io.summernova.admin.module.base.mapper;

import io.summernova.admin.core.mapper.MapperRelation;
import io.summernova.admin.core.mapper.RelationMapper;
import io.summernova.admin.module.base.model.Permission;
import io.summernova.admin.module.base.model.Role;
import org.apache.ibatis.annotations.Mapper;

@Mapper
@MapperRelation(table = "role_permission",
        field1 = "role_id", class1 = Role.class,
        field2 = "perm_id", class2 = Permission.class)
public interface RolePermissionMapper extends RelationMapper {
}
