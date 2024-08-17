package com.hzboiler.erp.module.base.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hzboiler.erp.core.context.BaseContext;
import com.hzboiler.erp.core.exception.BusinessException;
import com.hzboiler.erp.core.service.AbstractBaseService;
import com.hzboiler.erp.module.base.model.Role;
import com.hzboiler.erp.module.base.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.hzboiler.erp.core.exception.CoreBusinessExceptionEnums.ERROR_ACCESS_DENIED;

@Slf4j
@Service
public class UserServiceImpl extends AbstractBaseService<User> implements UserService {

    @Autowired
    RoleService roleService;

    @Autowired
    PermissionService permissionService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public User loadUserByUserId(Long userId) {
        return selectById(userId);
    }

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        // bypass model access check by using mapper
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getUsername, username);
        User user = getBaseMapper().selectOne(lambdaQueryWrapper);
        if (user == null)
            throw new UsernameNotFoundException("");

        return user;
    }

    @Override
    @Transactional
    public boolean createOne(User user) {
        if (user == null) return false;

        // Encode password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        boolean result = super.createOne(user);

        // Add default roles
        List<Role> defaultRoles = roleService.getDefaultRoles();
        roleService.addUserRoles(user.getId(), defaultRoles.stream().map(Role::getId).collect(Collectors.toSet()));

        return result;
    }

    @Override
    public boolean updateById(Long id, User user) {
        if (user == null) return false;

        // Current user can only update info of himself
        BaseContext context = getContext();
        if (!context.isAdmin() && !Objects.equals(id, context.getUserId()))
            throw new BusinessException(ERROR_ACCESS_DENIED);

        // Encode password
        if (user.getPassword() != null)
            user.setPassword(passwordEncoder.encode(user.getPassword()));

        return super.updateById(id, user);
    }
}
