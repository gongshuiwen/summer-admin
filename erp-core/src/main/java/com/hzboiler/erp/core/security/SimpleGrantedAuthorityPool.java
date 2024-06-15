package com.hzboiler.erp.core.security;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Static utility class which supports method for obtain shared {@link SimpleGrantedAuthority} instance.
 *
 * @author gongshuiwen
 */
public final class SimpleGrantedAuthorityPool {

    // pool
    private static final Map<String, SimpleGrantedAuthority> pool = new ConcurrentHashMap<>();

    // prevent instantiation
    private SimpleGrantedAuthorityPool() {
    }

    public static SimpleGrantedAuthority of(String authority) {
        authority = authority.intern();
        return pool.computeIfAbsent(authority, SimpleGrantedAuthority::new);
    }
}
