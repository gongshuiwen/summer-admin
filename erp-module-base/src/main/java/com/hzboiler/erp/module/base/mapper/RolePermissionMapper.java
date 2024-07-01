package com.hzboiler.erp.module.base.mapper;

import com.hzboiler.erp.core.mapper.MapperRelation;
import com.hzboiler.erp.core.mapper.RelationMapper;
import com.hzboiler.erp.module.base.model.Permission;
import com.hzboiler.erp.module.base.model.Role;
import org.apache.ibatis.annotations.Mapper;

@Mapper
@MapperRelation(table = "role_permission",
        field1 = "role_id", class1 = Role.class,
        field2 = "perm_id", class2 = Permission.class)
public interface RolePermissionMapper extends RelationMapper {
}
