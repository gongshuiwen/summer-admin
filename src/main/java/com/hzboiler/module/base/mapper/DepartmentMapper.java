package com.hzboiler.module.base.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hzboiler.module.base.model.Department;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DepartmentMapper extends BaseMapper<Department> {
}
