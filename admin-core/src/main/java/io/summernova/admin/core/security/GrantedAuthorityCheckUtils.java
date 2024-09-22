package io.summernova.admin.core.security;

import io.summernova.admin.core.context.BaseContextHolder;
import io.summernova.admin.core.security.authorization.BaseAuthority;

import java.util.Collection;

/**
 * @author gongshuiwen
 */
public final class GrantedAuthorityCheckUtils {

    // prevent instantiation
    private GrantedAuthorityCheckUtils() {
    }

    public static boolean isAdmin() {
        return BaseContextHolder.getContext().isAdmin();
    }

    public static boolean contains(BaseAuthority authority) {
        return getUserAuthorities().contains(authority);
    }

    public static boolean containsAny(Collection<? extends BaseAuthority> authorities) {
        Collection<? extends BaseAuthority> userAuthorities = getUserAuthorities();
        return authorities.stream().anyMatch(userAuthorities::contains);
    }

    public static boolean containsAll(Collection<? extends BaseAuthority> authorities) {
        Collection<? extends BaseAuthority> userAuthorities = getUserAuthorities();
        return userAuthorities.containsAll(authorities);
    }

    private static Collection<? extends BaseAuthority> getUserAuthorities() {
        return BaseContextHolder.getContext().getAuthorities();
    }
}
