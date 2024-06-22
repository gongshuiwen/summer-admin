package com.hzboiler.erp.core.security;

import com.hzboiler.erp.core.context.BaseContextHolder;
import org.springframework.security.core.GrantedAuthority;

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

    public static boolean contains(GrantedAuthority authority) {
        return getUserAuthorities().contains(authority);
    }

    public static boolean containsAny(Collection<? extends GrantedAuthority> authorities) {
        Collection<? extends GrantedAuthority> userAuthorities = getUserAuthorities();
        return authorities.stream().anyMatch(userAuthorities::contains);
    }

    public static boolean containsAll(Collection<? extends GrantedAuthority> authorities) {
        Collection<? extends GrantedAuthority> userAuthorities = getUserAuthorities();
        return userAuthorities.containsAll(authorities);
    }

    private static Collection<? extends GrantedAuthority> getUserAuthorities() {
        return BaseContextHolder.getContext().getAuthorities();
    }
}
