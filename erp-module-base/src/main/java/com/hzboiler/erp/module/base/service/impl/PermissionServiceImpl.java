package com.hzboiler.erp.module.base.service.impl;

import com.hzboiler.erp.core.service.AbstractBaseService;
import com.hzboiler.erp.module.base.mapper.PermissionMapper;
import com.hzboiler.erp.module.base.mapper.RolePermissionMapper;
import com.hzboiler.erp.module.base.model.Permission;
import com.hzboiler.erp.module.base.model.Role;
import com.hzboiler.erp.module.base.service.PermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PermissionServiceImpl extends AbstractBaseService<PermissionMapper, Permission> implements PermissionService {

    @Autowired
    RolePermissionMapper rolePermissionMapper;

    @Override
    @Transactional
    public Set<Permission> getPermissionsByRoleId(Long roleId) {
        Objects.requireNonNull(roleId, "roleId must not be null");
        List<Long> permIds = rolePermissionMapper.getTargetIds(Role.class, roleId);
        return getBaseMapper().selectBatchIds(permIds).stream().collect(Collectors.toUnmodifiableSet());
    }

    @Override
    @Transactional
    public Set<Permission> getPermissionsByRoleIds(Set<Long> roleIds) {
        Objects.requireNonNull(roleIds, "roleIds must not be null");
        if (roleIds.isEmpty()) throw new IllegalArgumentException("roleIds must not be empty");

        List<Long> permIds = rolePermissionMapper.getTargetIds(Role.class, roleIds.stream().toList());
        if (permIds.isEmpty())
            return Set.of();

        return getBaseMapper().selectBatchIds(permIds).stream().collect(Collectors.toUnmodifiableSet());
    }

    @Override
    @Transactional
    public void addRolePermissions(Long roleId, Set<Long> permIds) {
        Objects.requireNonNull(roleId, "roleId must not be null");
        Objects.requireNonNull(permIds, "permIds must not be null");
        if (permIds.isEmpty()) throw new IllegalArgumentException("permIds must not be empty");
        rolePermissionMapper.add(Role.class, roleId, permIds.stream().toList());
    }

    @Override
    @Transactional
    public void removeRolePermissions(Long roleId, Set<Long> permIds) {
        Objects.requireNonNull(roleId, "roleId must not be null");
        Objects.requireNonNull(permIds, "permIds must not be null");
        if (permIds.isEmpty()) throw new IllegalArgumentException("permIds must not be empty");
        rolePermissionMapper.remove(Role.class, roleId, permIds.stream().toList());
    }

    @Override
    @Transactional
    public void replaceRolePermissions(Long roleId, Set<Long> permIds) {
        Objects.requireNonNull(roleId, "roleId must not be null");
        Objects.requireNonNull(permIds, "permIds must not be null");
        rolePermissionMapper.replace(Role.class, roleId, permIds.stream().toList());
    }
}
