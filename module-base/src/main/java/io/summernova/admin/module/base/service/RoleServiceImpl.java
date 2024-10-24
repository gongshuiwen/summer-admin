package io.summernova.admin.module.base.service;

import io.summernova.admin.common.exception.ValidationException;
import io.summernova.admin.core.service.AbstractBaseService;
import io.summernova.admin.module.base.model.Role;
import io.summernova.admin.module.base.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static io.summernova.admin.core.security.Constants.CODE_BASE_USER;

/**
 * @author gongshuiwen
 */
@Slf4j
@Service
public class RoleServiceImpl extends AbstractBaseService<Role> implements RoleService {

    private final Field rolesField = User.class.getDeclaredField("roles");

    public RoleServiceImpl() throws NoSuchFieldException {
    }

    @Override
    @Transactional
    public Set<Role> getRolesByUserId(Long userId) {
        Objects.requireNonNull(userId, "userId must not be null");

        List<Long> roleIds = getRelationMapper(rolesField).getTargetIds(List.of(userId));
        if (roleIds == null || roleIds.isEmpty())
            return Set.of();

        // bypass model access check by using mapper
        return getBaseMapper().selectBatchIds(roleIds).stream().collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public Role getRoleByCode(String code) {
        return lambdaQuery().eq(Role::getCode, code).one();
    }

    @Override
    public List<Role> getDefaultRoles() {
        Role baseRole = lambdaQuery().eq(Role::getCode, CODE_BASE_USER).one();
        if (baseRole == null)
            throw new ValidationException("默认角色不存在");

        return List.of(baseRole);
    }

    @Override
    @Transactional
    public void addUserRoles(Long userId, Set<Long> roleIds) {
        Objects.requireNonNull(userId, "userId must not be null");
        Objects.requireNonNull(roleIds, "roleIds must not be null");
        if (roleIds.isEmpty()) throw new IllegalArgumentException("roleIds must not be empty");
        getRelationMapper(rolesField).add(userId, roleIds.stream().toList());
    }

    @Override
    @Transactional
    public void removeUserRoles(Long userId, Set<Long> roleIds) {
        Objects.requireNonNull(userId, "userId must not be null");
        Objects.requireNonNull(roleIds, "roleIds must not be null");
        if (roleIds.isEmpty()) throw new IllegalArgumentException("roleIds must not be empty");
        getRelationMapper(rolesField).remove(userId, roleIds.stream().toList());
    }

    @Override
    @Transactional
    public void replaceUserRoles(Long userId, Set<Long> roleIds) {
        Objects.requireNonNull(userId, "userId must not be null");
        Objects.requireNonNull(roleIds, "roleIds must not be null");
        getRelationMapper(rolesField).replace(userId, roleIds.stream().toList());
    }
}
