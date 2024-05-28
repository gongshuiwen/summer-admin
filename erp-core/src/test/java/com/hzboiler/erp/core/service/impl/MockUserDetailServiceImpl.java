package com.hzboiler.erp.core.service.impl;

import com.hzboiler.erp.core.model.BaseUser;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MockUserDetailServiceImpl implements UserDetailsService {
    @Override
    public BaseUser loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}
