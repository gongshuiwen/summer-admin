package com.hzboiler.module.base.service;

import com.hzboiler.module.base.model.User;
import com.hzboiler.core.service.BaseService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserService extends BaseService<User>, UserDetailsService {

    @Override
    User loadUserByUsername(String username) throws UsernameNotFoundException;
}
