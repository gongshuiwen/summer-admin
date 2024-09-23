package io.summernova.admin.spring.security;

import io.summernova.admin.core.security.authorization.BaseAuthoritiesService;
import io.summernova.admin.core.security.authorization.BaseAuthority;
import io.summernova.admin.core.security.authorization.SimpleAuthority;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author gongshuiwen
 */
@Component
@Profile("test")
public class SpringSecurityContextBaseAuthoritiesService implements BaseAuthoritiesService {

    @Override
    public Set<? extends BaseAuthority> loadAuthoritiesByUserId(Long userId) {
        // Get authorities from security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() ) {
            Collection<? extends GrantedAuthority> authorities1 = authentication.getAuthorities();
            if (authorities1 != null && !authorities1.isEmpty()) {
                return authorities1.stream()
                        .map(GrantedAuthority::getAuthority)
                        .map(SimpleAuthority::of)
                        .collect(Collectors.toUnmodifiableSet());
            }
        }

        return Set.of();
    }
}
