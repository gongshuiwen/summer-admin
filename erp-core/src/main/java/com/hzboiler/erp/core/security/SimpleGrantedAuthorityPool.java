package com.hzboiler.erp.core.security;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Static utility class for getting shared {@link SimpleGrantedAuthority} objects.
 *
 * @author gongshuiwen
 */
public final class SimpleGrantedAuthorityPool {

    // pool
    private static final Map<String, SimpleGrantedAuthority> pool = new ConcurrentHashMap<>();

    // prevent external instantiation
    private SimpleGrantedAuthorityPool() {
    }

    public static SimpleGrantedAuthority getAuthority(String authority) {
        // TODO: jmh to different implementation
        // return pool.computeIfAbsent(authority, k -> new SimpleGrantedAuthority(k))
        // return pool.computeIfAbsent(authority, k -> new SimpleGrantedAuthority(k.intern()))
        return pool.computeIfAbsent(authority.intern(), SimpleGrantedAuthority::new);
    }
}
