package com.hzboiler.erp.module.base.service.impl;

import com.hzboiler.erp.core.exception.ValidationException;
import com.hzboiler.erp.core.service.AbstractBaseService;
import com.hzboiler.erp.module.base.mapper.RoleMapper;
import com.hzboiler.erp.module.base.mapper.UserRoleMapper;
import com.hzboiler.erp.module.base.model.Role;
import com.hzboiler.erp.module.base.model.User;
import com.hzboiler.erp.module.base.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.hzboiler.erp.core.security.Constants.CODE_BASE_USER;

@Slf4j
@Service
public class RoleServiceImpl extends AbstractBaseService<RoleMapper, Role> implements RoleService {

    @Autowired
    UserRoleMapper userRoleMapper;

    @Override
    @Transactional
    public Set<Role> getRolesByUserId(Long userId) {
        Objects.requireNonNull(userId, "userId must not be null");

        List<Long> roleIds = userRoleMapper.getTargetIds(User.class, List.of(userId));
        if (roleIds == null || roleIds.isEmpty())
            return Set.of();

        return getMapper().selectBatchIds(roleIds).stream().collect(Collectors.toUnmodifiableSet());
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
        if (userId == null || roleIds == null || roleIds.isEmpty()) {
            throw new IllegalArgumentException();
        }
        roleIds.removeAll(userRoleMapper.getRoleIdsByUserId(userId));
        if (roleIds.isEmpty()) {
            return;
        }
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
