package com.hzhg.plm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hzhg.plm.entity.Permission;
import com.hzhg.plm.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PermissionMapper extends BaseMapper<Permission> {

    @Select("SELECT * FROM permission LEFT JOIN role_permission ON permission.id = role_permission.perm_id " +
            "WHERE role_permission.role_id in #{roleIds}")
    List<Permission> getPermissionsByRoleIds(List<Long> roleIds);
}
