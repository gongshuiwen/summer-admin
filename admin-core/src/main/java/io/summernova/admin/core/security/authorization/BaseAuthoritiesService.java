package io.summernova.admin.core.security.authorization;

import org.springframework.security.core.GrantedAuthority;

import java.util.Set;

/**
 * @author gongshuiwen
 */
public interface BaseAuthoritiesService {

    /**
     * Get the set of {@link GrantedAuthority} by user id.
     * @param userId id of user
     * @return set of {@link GrantedAuthority}
     */
    Set<? extends BaseAuthority> loadAuthoritiesByUserId(Long userId);
}
