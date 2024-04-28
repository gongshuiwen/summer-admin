package com.hzboiler.core.security;

import com.hzboiler.core.context.BaseContext;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class GrantedAuthorityCheckUtils {

    public static boolean contains(GrantedAuthority authority) {
        Collection<? extends GrantedAuthority> userAuthorities = getUserAuthorities();
        if (userAuthorities == null) return false;
        return userAuthorities.contains(authority);
    }

    public static boolean containsAny(Collection<? extends GrantedAuthority> authorities) {
        Collection<? extends GrantedAuthority> userAuthorities = getUserAuthorities();
        if (userAuthorities == null) return false;
        return authorities.stream().anyMatch(userAuthorities::contains);
    }

    public static boolean containsAll(Collection<? extends GrantedAuthority> authorities) {
        Collection<? extends GrantedAuthority> userAuthorities = getUserAuthorities();
        if (userAuthorities == null) return false;
        return userAuthorities.containsAll(authorities);
    }

    private static Collection<? extends GrantedAuthority> getUserAuthorities() {
        UserDetails user = BaseContext.getUserDetails();
        if (user != null){
            Collection<? extends GrantedAuthority> userAuthorities = user.getAuthorities();
            if (userAuthorities == null || userAuthorities.isEmpty()) {
                return null;
            }
            return userAuthorities;
        }

        return null;
    }
}
