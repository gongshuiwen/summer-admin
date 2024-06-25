package com.hzboiler.erp.core.security;

import com.hzboiler.erp.core.model.BaseUser;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * @author gongshuiwen
 */
@Component
@Profile("test")
public class SecurityContextBaseUserService implements BaseUserService {

    @Override
    public BaseUser selectById(Long userId) {
        // Get user info from security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()
                && authentication.getPrincipal() instanceof BaseUser baseUser) {
            return baseUser;
        }
        return null;
    }
}
