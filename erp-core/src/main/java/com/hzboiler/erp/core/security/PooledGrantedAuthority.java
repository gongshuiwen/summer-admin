package com.hzboiler.erp.core.security;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Assert;

import java.io.Serial;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author gongshuiwen
 */
@Getter
public final class PooledGrantedAuthority implements GrantedAuthority {

    private static final Map<String, PooledGrantedAuthority> pool = new ConcurrentHashMap<>();

    @Serial
    private static final long serialVersionUID = 1L;

    private final String authority;

    private PooledGrantedAuthority(String authority) {
        Assert.hasText(authority, "A granted authority textual representation is required");
        this.authority = authority;
    }

    public static PooledGrantedAuthority of(String authority) {
        authority = authority.intern();
        return pool.computeIfAbsent(authority, PooledGrantedAuthority::new);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof PooledGrantedAuthority pga) {
            return authority.equals(pga.authority);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return authority.hashCode();
    }

    @Override
    public String toString() {
        return authority;
    }
}
