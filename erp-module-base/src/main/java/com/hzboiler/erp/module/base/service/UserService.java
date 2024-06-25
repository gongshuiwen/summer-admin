package com.hzboiler.erp.module.base.service;

import com.hzboiler.erp.core.security.BaseUserService;
import com.hzboiler.erp.module.base.model.User;
import com.hzboiler.erp.core.service.BaseService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserService extends BaseService<User>, UserDetailsService, BaseUserService {

    @Override
    User loadUserByUsername(String username) throws UsernameNotFoundException;
}
