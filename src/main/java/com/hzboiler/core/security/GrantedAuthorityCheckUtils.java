package com.hzboiler.core.security;

import com.hzboiler.core.context.BaseContextHolder;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * @author gongshuiwen
 */
public class GrantedAuthorityCheckUtils {

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

    public static boolean containsAll(Collection<GrantedAuthority> authorities) {
        Collection<? extends GrantedAuthority> userAuthorities = getUserAuthorities();
        return userAuthorities.containsAll(authorities);
    }

    private static Collection<? extends GrantedAuthority> getUserAuthorities() {
        return BaseContextHolder.getContext().getAuthorities();
    }
}
