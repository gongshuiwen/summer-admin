package io.summernova.admin.module.base.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.summernova.admin.common.exception.BusinessException;
import io.summernova.admin.core.context.BaseContext;
import io.summernova.admin.core.service.AbstractBaseService;
import io.summernova.admin.module.base.model.Role;
import io.summernova.admin.module.base.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static io.summernova.admin.common.exception.CoreBusinessExceptionEnums.ERROR_ACCESS_DENIED;

/**
 * @author gongshuiwen
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends AbstractBaseService<User> implements UserService {

    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public User loadUserByUserId(Long userId) {
        return selectById(userId);
    }

    @Override
    public User loadUserByUsername(String username) {
        // bypass model access check by using mapper
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getUsername, username);
        return getBaseMapper().selectOne(lambdaQueryWrapper);
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
