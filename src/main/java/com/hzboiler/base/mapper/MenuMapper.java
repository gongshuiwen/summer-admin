package com.hzboiler.base.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hzboiler.base.model.Menu;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MenuMapper extends BaseMapper<Menu> {
}