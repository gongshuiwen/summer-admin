package com.hzboiler.erp.core.service;

import com.hzboiler.erp.core.security.GrantedAuthoritiesService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Set;

@Component
public class SecurityContextGrantedAuthoritiesService implements GrantedAuthoritiesService {

    @Override
    public Set<? extends GrantedAuthority> getAuthoritiesByUserId(Long userId) {
        // Get authorities from security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() ) {
            Collection<? extends GrantedAuthority> authorities1 = authentication.getAuthorities();
            if (authorities1 != null && !authorities1.isEmpty()) {
                return Set.copyOf(authorities1);
            }
        }

        return Set.of();
    }
}
