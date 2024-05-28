package com.hzboiler.module.base.service.impl;

import com.hzboiler.module.base.model.Permission;
import com.hzboiler.module.base.model.Role;
import com.hzboiler.module.base.mapper.RolePermissionMapper;
import com.hzboiler.core.service.AbstractBaseService;
import com.hzboiler.module.base.mapper.PermissionMapper;
import com.hzboiler.module.base.service.PermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PermissionServiceImpl extends AbstractBaseService<PermissionMapper, Permission> implements PermissionService {

    @Autowired
    PermissionMapper permissionMapper;

    @Autowired
    RolePermissionMapper rolePermissionMapper;

    @Override
    @Transactional
    public Set<Permission> getPermissionsByRoleId(Long roleId) {
        if (roleId == null) {
            throw new IllegalArgumentException();
        }
        Set<Long> permIds = rolePermissionMapper.getPermissionIdsByRoleId(roleId);
        return new HashSet<>(permissionMapper.selectBatchIds(permIds));
    }

    @Override
    @Transactional
    public Set<Permission> getPermissionsByRoleIds(Set<Long> roleIds) {
        if (roleIds == null) {
            throw new IllegalArgumentException();
        }
        Set<Long> permIds = rolePermissionMapper.getPermissionIdsByRoleIds(roleIds);
        if (permIds.isEmpty()) {
            return Set.of();
        }

        return permissionMapper.selectBatchIds(permIds).stream().collect(Collectors.toUnmodifiableSet());
    }

    @Override
    @Transactional
    public void addRolePermissions(Long roleId, Set<Long> permIds) {
        if (roleId == null || permIds == null || permIds.isEmpty()) {
            throw new IllegalArgumentException();
        }
        Set<Long> existPermIds = rolePermissionMapper.getPermissionIdsByRoleId(roleId);
        rolePermissionMapper.add(Role.class, roleId, permIds.stream().filter(id -> !existPermIds.contains(id)).toList());
    }

    @Override
    @Transactional
    public void removeRolePermissions(Long roleId, Set<Long> permIds) {
        if (roleId == null || permIds == null || permIds.isEmpty()) {
            throw new IllegalArgumentException();
        }
        rolePermissionMapper.removeRolePermissions(roleId, permIds);
    }

    @Override
    @Transactional
    public void replaceRolePermissions(Long roleId, Set<Long> permIds) {
        if (roleId == null || permIds == null || permIds.isEmpty()) {
            throw new IllegalArgumentException();
        }
        rolePermissionMapper.removeRolePermissionsAll(roleId);
        rolePermissionMapper.addRolePermissions(roleId, permIds);
    }
}
