package io.summernova.admin.module.base.security;

import io.summernova.admin.core.security.authorization.GrantedAuthoritiesService;
import io.summernova.admin.core.security.authorization.SimpleGrantedAuthority;
import io.summernova.admin.module.base.model.Permission;
import io.summernova.admin.module.base.model.Role;
import io.summernova.admin.module.base.service.PermissionService;
import io.summernova.admin.module.base.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static io.summernova.admin.core.security.Constants.ROLE_PREFIX;

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
                .map(role -> SimpleGrantedAuthority.of(ROLE_PREFIX + role.getCode()))
                .forEach(authorities::add);

        // Get authorities from permissions
        Set<Long> roleIds = roles.stream().map(Role::getId).collect(Collectors.toSet());
        Set<Permission> permissions = permissionService.getPermissionsByRoleIds(roleIds);
        permissions.stream()
                .map(permission -> SimpleGrantedAuthority.of(permission.getCode()))
                .forEach(authorities::add);

        return Collections.unmodifiableSet(authorities);
    }
}
