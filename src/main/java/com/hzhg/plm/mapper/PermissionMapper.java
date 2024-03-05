package com.hzhg.plm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hzhg.plm.entity.Permission;
import org.apache.ibatis.annotations.Mapper;

import java.util.Set;

@Mapper
public interface PermissionMapper extends BaseMapper<Permission> {

    Set<Permission> getPermissionsByRoleId(Long roleId);
}
