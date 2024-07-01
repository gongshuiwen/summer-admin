package com.hzboiler.erp.module.base.mapper;

import com.hzboiler.erp.core.mapper.MapperRelation;
import com.hzboiler.erp.core.mapper.RelationMapper;
import com.hzboiler.erp.module.base.model.Role;
import com.hzboiler.erp.module.base.model.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
@MapperRelation(table = "user_role",
        field1 = "user_id", class1 = User.class,
        field2 = "role_id", class2 = Role.class)
public interface UserRoleMapper extends RelationMapper {
}
