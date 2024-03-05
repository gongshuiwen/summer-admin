package com.hzhg.plm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hzhg.plm.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserService extends IService<User>, UserDetailsService {

    @Override
    User loadUserByUsername(String username) throws UsernameNotFoundException;
}
