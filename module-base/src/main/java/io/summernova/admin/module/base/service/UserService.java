package io.summernova.admin.module.base.service;

import io.summernova.admin.core.security.account.BaseUserService;
import io.summernova.admin.core.service.BaseService;
import io.summernova.admin.module.base.model.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserService extends BaseService<User>, BaseUserService {

    @Override
    User loadUserByUserId(Long id);

    @Override
    User loadUserByUsername(String username) throws UsernameNotFoundException;
}