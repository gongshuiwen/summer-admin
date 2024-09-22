package io.summernova.admin.module.base.security;

import io.summernova.admin.core.security.authorization.BaseAuthoritiesService;
import io.summernova.admin.core.security.authorization.BaseAuthority;
import io.summernova.admin.core.security.authorization.SimpleAuthority;
import io.summernova.admin.module.base.model.Permission;
import io.summernova.admin.module.base.model.Role;
import io.summernova.admin.module.base.service.PermissionService;
import io.summernova.admin.module.base.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static io.summernova.admin.core.security.Constants.ROLE_PREFIX;

/**
 * @author gongshuiwen
 */
@Service
@RequiredArgsConstructor
@Profile("!test")
public class GrantedAuthoritiesServiceImpl implements BaseAuthoritiesService {

    private final RoleService roleService;
    private final PermissionService permissionService;

    @Override
    public Set<? extends BaseAuthority> loadAuthoritiesByUserId(Long userId) {
        Set<BaseAuthority> authorities = new HashSet<>();

        // Get authorities from roles
        Set<Role> roles = roleService.getRolesByUserId(userId);
        roles.stream()
                .map(role -> SimpleAuthority.of(ROLE_PREFIX + role.getCode()))
                .forEach(authorities::add);

        // Get authorities from permissions
        Set<Long> roleIds = roles.stream().map(Role::getId).collect(Collectors.toSet());
        Set<Permission> permissions = permissionService.getPermissionsByRoleIds(roleIds);
        permissions.stream()
                .map(permission -> SimpleAuthority.of(permission.getCode()))
                .forEach(authorities::add);

        return Collections.unmodifiableSet(authorities);
    }
}
