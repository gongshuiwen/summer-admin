package com.hzhg.plm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hzhg.plm.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Set;

@Mapper
public interface RoleMapper extends BaseMapper<Role> {


    Set<Role> getRolesByUserId(Long userId);
}
