package com.hzhg.plm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hzhg.plm.entity.Role;
import com.hzhg.plm.entity.User;
import com.hzhg.plm.mapper.RoleMapper;
import com.hzhg.plm.mapper.UserMapper;
import com.hzhg.plm.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    RoleMapper roleMapper;

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        User user = this.getOne(wrapper);
        if (user == null) {
            throw new UsernameNotFoundException("");
        }

        // Set roles
        List<Role> roles = roleMapper.getRolesByUserId(user.getId());
        user.setRoles(roles);

        // Set authorities
        user.setAuthoritiesWithRoles(roles);
        return user;
    }
}
