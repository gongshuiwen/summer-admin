package io.summernova.admin.module.base.service;

import io.summernova.admin.core.service.AbstractBaseService;
import io.summernova.admin.module.base.mapper.RolePermissionMapper;
import io.summernova.admin.module.base.model.Permission;
import io.summernova.admin.module.base.model.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author gongshuiwen
 */
@Slf4j
@Service
public class PermissionServiceImpl extends AbstractBaseService<Permission> implements PermissionService {

    private final RolePermissionMapper rolePermissionMapper;

    public PermissionServiceImpl(RolePermissionMapper rolePermissionMapper) {
        this.rolePermissionMapper = rolePermissionMapper;
    }

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
