package io.summernova.admin.module.base.mapper;

import io.summernova.admin.core.mapper.MapperRelation;
import io.summernova.admin.core.mapper.RelationMapper;
import io.summernova.admin.module.base.model.Role;
import io.summernova.admin.module.base.model.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
@MapperRelation(table = "user_role",
        field1 = "user_id", class1 = User.class,
        field2 = "role_id", class2 = Role.class)
public interface UserRoleMapper extends RelationMapper {
}
