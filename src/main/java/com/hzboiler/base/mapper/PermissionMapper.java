package com.hzboiler.base.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hzboiler.base.model.Permission;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PermissionMapper extends BaseMapper<Permission> {
}
