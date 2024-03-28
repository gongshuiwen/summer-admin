package com.hzhg.plm.service.impl;

import com.hzhg.plm.core.service.AbstractBaseService;
import com.hzhg.plm.entity.Permission;
import com.hzhg.plm.mapper.PermissionMapper;
import com.hzhg.plm.mapper.RolePermissionMapper;
import com.hzhg.plm.service.PermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

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
        return new HashSet<>(permissionMapper.selectBatchIds(permIds));
    }

    @Override
    @Transactional
    public void addRolePermissions(Long roleId, Set<Long> permIds) {
        if (roleId == null || permIds == null || permIds.isEmpty()) {
            throw new IllegalArgumentException();
        }
        permIds.removeAll(rolePermissionMapper.getPermissionIdsByRoleId(roleId));
        rolePermissionMapper.addRolePermissions(roleId, permIds);
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
