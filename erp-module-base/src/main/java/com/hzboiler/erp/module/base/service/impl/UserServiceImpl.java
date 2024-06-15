package com.hzboiler.erp.module.base.service.impl;

import com.hzboiler.erp.module.base.model.Role;
import com.hzboiler.erp.module.base.model.User;
import com.hzboiler.erp.core.service.AbstractBaseService;
import com.hzboiler.erp.module.base.mapper.UserMapper;
import com.hzboiler.erp.module.base.service.PermissionService;
import com.hzboiler.erp.module.base.service.RoleService;
import com.hzboiler.erp.module.base.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

import static com.hzboiler.erp.core.security.Constants.CODE_BASE_USER;

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
        User user = lambdaQuery().eq(User::getUsername, username).one();
        if (user == null) {
            throw new UsernameNotFoundException("");
        }
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
