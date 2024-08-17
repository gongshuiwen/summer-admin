package com.hzboiler.erp.core.security.account;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * @author gongshuiwen
 */
public interface BaseUserService extends UserDetailsService {

    /**
     * Load {@link BaseUser} by user id.
     * @param userId id of user
     * @return {@link BaseUser}
     */
    BaseUser loadUserByUserId(Long userId);

    @Override
    BaseUser loadUserByUsername(String username) throws UsernameNotFoundException;
}
