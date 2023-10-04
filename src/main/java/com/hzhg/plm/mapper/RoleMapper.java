package com.hzhg.plm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hzhg.plm.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RoleMapper extends BaseMapper<Role> {

    @Select("SELECT * FROM role LEFT JOIN user_role ON role.id = user_role.role_id " +
            "WHERE user_role.user_id = #{userId}")
    List<Role> getRolesByUserId(Long userId);
}
