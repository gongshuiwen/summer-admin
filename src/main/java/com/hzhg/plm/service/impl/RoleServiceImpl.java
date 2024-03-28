package com.hzhg.plm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hzhg.plm.core.service.AbstractBaseService;
import com.hzhg.plm.entity.Role;
import com.hzhg.plm.mapper.RoleMapper;
import com.hzhg.plm.mapper.UserRoleMapper;
import com.hzhg.plm.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
public class RoleServiceImpl extends AbstractBaseService<RoleMapper, Role> implements RoleService {

    @Autowired
    RoleMapper roleMapper;

    @Autowired
    UserRoleMapper userRoleMapper;

    @Override
    public Set<Role> getRolesByUserId(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException();
        }
        Set<Long> roleIds = userRoleMapper.getRoleIdsByUserId(userId);
        if (roleIds == null || roleIds.isEmpty()) {
            return new HashSet<>();
        }
        return new HashSet<>(roleMapper.selectBatchIds(roleIds));
    }

    @Override
    public Role getRoleByCode(String code) {
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        return roleMapper.selectOne(queryWrapper.eq(Role::getCode, code));
    }

    @Override
    @Transactional
    public void addUserRoles(Long userId, Set<Long> roleIds) {
        if (userId == null || roleIds == null || roleIds.isEmpty()) {
            throw new IllegalArgumentException();
        }
        roleIds.removeAll(userRoleMapper.getRoleIdsByUserId(userId));
        userRoleMapper.addUserRoles(userId, roleIds);
    }

    @Override
    @Transactional
    public void removeUserRoles(Long userId, Set<Long> roleIds) {
        if (userId == null || roleIds == null || roleIds.isEmpty()) {
            throw new IllegalArgumentException();
        }
        userRoleMapper.removeUserRoles(userId, roleIds);
    }

    @Override
    @Transactional
    public void replaceUserRoles(Long userId, Set<Long> roleIds) {
        if (userId == null || roleIds == null || roleIds.isEmpty()) {
            throw new IllegalArgumentException();
        }
        userRoleMapper.removeUserRolesAll(userId);
        userRoleMapper.addUserRoles(userId, roleIds);
    }
}
