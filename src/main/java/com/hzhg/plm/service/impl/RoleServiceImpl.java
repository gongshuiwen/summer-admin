package com.hzhg.plm.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hzhg.plm.entity.Role;
import com.hzhg.plm.mapper.RoleMapper;
import com.hzhg.plm.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Slf4j
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    RoleMapper roleMapper;

    @Override
    public Set<Role> getRolesByUserId(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException();
        }
        return roleMapper.getRolesByUserId(userId);
    }
}
