package com.hzboiler.erp.module.base.security;

import com.hzboiler.erp.core.security.SimpleGrantedAuthorityPool;
import com.hzboiler.erp.core.security.authorization.GrantedAuthoritiesService;
import com.hzboiler.erp.module.base.model.Permission;
import com.hzboiler.erp.module.base.model.Role;
import com.hzboiler.erp.module.base.service.PermissionService;
import com.hzboiler.erp.module.base.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static com.hzboiler.erp.core.security.Constants.ROLE_PREFIX;

@Service
@RequiredArgsConstructor
@Profile("!test")
public class GrantedAuthoritiesServiceImpl implements GrantedAuthoritiesService {

    private final RoleService roleService;
    private final PermissionService permissionService;

    @Override
    public Set<? extends GrantedAuthority> getAuthoritiesByUserId(Long userId) {
        Set<GrantedAuthority> authorities = new HashSet<>();

        // Get authorities from roles
        Set<Role> roles = roleService.getRolesByUserId(userId);
        roles.stream()
                .map(role -> SimpleGrantedAuthorityPool.getAuthority(ROLE_PREFIX + role.getCode()))
                .forEach(authorities::add);

        // Get authorities from permissions
        Set<Long> roleIds = roles.stream().map(Role::getId).collect(Collectors.toSet());
        Set<Permission> permissions = permissionService.getPermissionsByRoleIds(roleIds);
        permissions.stream()
                .map(permission -> SimpleGrantedAuthorityPool.getAuthority(permission.getCode()))
                .forEach(authorities::add);

        return Collections.unmodifiableSet(authorities);
    }
}
