package io.summernova.admin.core.security.authorization;

import java.util.Set;

/**
 * @author gongshuiwen
 */
public interface BaseAuthoritiesService {

    /**
     * Get the set of {@link BaseAuthority} by user id.
     * @param userId id of user
     * @return set of {@link BaseAuthority}
     */
    Set<? extends BaseAuthority> loadAuthoritiesByUserId(Long userId);
}
