package com.hzhg.plm.service;

import com.hzhg.plm.core.service.BaseService;
import com.hzhg.plm.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserService extends BaseService<User>, UserDetailsService {

    @Override
    User loadUserByUsername(String username) throws UsernameNotFoundException;
}
