package com.hzhg.plm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hzhg.plm.core.fields.Many2Many;
import com.hzhg.plm.core.service.AbstractBaseService;
import com.hzhg.plm.entity.Permission;
import com.hzhg.plm.entity.Role;
import com.hzhg.plm.entity.User;
import com.hzhg.plm.mapper.UserMapper;
import com.hzhg.plm.service.PermissionService;
import com.hzhg.plm.service.RoleService;
import com.hzhg.plm.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.hzhg.plm.core.utils.Constants.CODE_BASE_USER;

@Slf4j
@Service
public class UserServiceImpl extends AbstractBaseService<UserMapper, User> implements UserService {

    @Autowired
    RoleService roleService;

    @Autowired
    PermissionService permissionService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        User user = this.getOne(wrapper);
        if (user == null) {
            throw new UsernameNotFoundException("");
        }

        // Get and Set roles
        Set<Role> roles = roleService.getRolesByUserId(user.getId());
        user.setRoles(Many2Many.ofValues(roles.stream().toList()));

        // Get permissions
        Set<Long> roleIds = roles.stream().map(Role::getId).collect(Collectors.toSet());
        Set<Permission> permissions = permissionService.getPermissionsByRoleIds(roleIds);

        // Add authorities
        user.addAuthoritiesWithRoles(roles);
        user.addAuthoritiesWithPermissions(permissions);
        return user;
    }

    @Override
    @Transactional
    public boolean createOne(User user) {
        if (user == null) return false;

        // Encode password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        boolean result = super.createOne(user);

        // Add BASE_USER role
        Role baseUserRole = roleService.getRoleByCode(CODE_BASE_USER);
        if (baseUserRole == null) {
            throw new RuntimeException("BASE_USER role not found");
        }

        roleService.addUserRoles(user.getId(), new HashSet<>(List.of(baseUserRole.getId())));
        return result;
    }

    @Override
    public boolean updateById(Long id, User user) {
        if (user == null) return false;

        // Encode password
        if (user.getPassword() != null)
            user.setPassword(passwordEncoder.encode(user.getPassword()));

        return super.updateById(id, user);
    }
}
