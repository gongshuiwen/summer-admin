package io.summernova.admin.spring.security;

import io.summernova.admin.core.security.account.BaseUser;
import io.summernova.admin.core.security.account.BaseUserService;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * @author gongshuiwen
 */
@Component
@Profile("test")
@Primary
public class SpringSecurityContextBaseUserService implements BaseUserService {

    @Override
    public BaseUser loadUserByUserId(Long userId) {
        // Get user info from security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()
                && authentication.getPrincipal() instanceof BaseUser baseUser) {
            return baseUser;
        }
        return null;
    }
}
