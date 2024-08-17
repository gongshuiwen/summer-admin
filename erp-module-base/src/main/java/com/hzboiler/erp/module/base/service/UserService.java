package com.hzboiler.erp.module.base.service;

import com.hzboiler.erp.core.security.account.BaseUserService;
import com.hzboiler.erp.core.service.BaseService;
import com.hzboiler.erp.module.base.model.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserService extends BaseService<User>, BaseUserService {

    @Override
    User loadUserByUserId(Long id);

    @Override
    User loadUserByUsername(String username) throws UsernameNotFoundException;
}
