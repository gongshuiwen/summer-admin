package com.hzhg.plm.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hzhg.plm.entity.Permission;
import com.hzhg.plm.mapper.PermissionMapper;
import com.hzhg.plm.mapper.RolePermissionMapper;
import com.hzhg.plm.service.PermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.annotation.AccessType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {

    @Autowired
    PermissionService permissionService;

    @Autowired
    PermissionMapper permissionMapper;

    @Autowired
    RolePermissionMapper rolePermissionMapper;

    @Override
    public Set<Permission> getPermissionsByRoleId(Long roleId) {
        if (roleId == null) {
            throw new IllegalArgumentException();
        }
        return permissionMapper.getPermissionsByRoleId(roleId);
    }

    @Override
    public Set<Permission> getPermissionsByRoleIds(Set<Long> roleIds) {
        if (roleIds == null || roleIds.isEmpty()) {
            throw new IllegalArgumentException();
        }
        Set<Permission> permissions = new HashSet<>();
        for (Long roleId : roleIds) {
            permissions.addAll(permissionService.getPermissionsByRoleId(roleId));
        }
        return permissions;
    }

    @Override
    public void addRolePermissions(Long roleId, Set<Long> permIds) {
        if (roleId == null || permIds == null || permIds.isEmpty()) {
            throw new IllegalArgumentException();
        }
        Set<Long> existPermIds = permissionMapper.getPermissionsByRoleId(roleId)
                .stream().map(Permission::getId).collect(Collectors.toSet());
        permIds.removeAll(existPermIds);
        rolePermissionMapper.addRolePermissions(roleId, permIds);
    }

    @Override
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
