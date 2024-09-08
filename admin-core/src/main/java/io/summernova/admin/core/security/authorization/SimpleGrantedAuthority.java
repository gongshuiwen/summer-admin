package io.summernova.admin.core.security.authorization;

import org.springframework.security.core.GrantedAuthority;

import java.io.Serial;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Basic concrete implementation of a {@link GrantedAuthority}.
 *
 * <p>
 * Stores a {@code String} representation of an authority granted to the user.
 *
 * @author gongshuiwen
 */
public final class SimpleGrantedAuthority implements GrantedAuthority {

    @Serial
    private static final long serialVersionUID = 1234567890L;

    // pool
    private static final Map<String, SimpleGrantedAuthority> pool = new ConcurrentHashMap<>();

    private final String role;

    private SimpleGrantedAuthority(String role) {
        if (role == null || role.isBlank())
            throw new IllegalArgumentException("Role string cannot be null or blank");
        this.role = role;
    }

    public static SimpleGrantedAuthority of(String role) {
        // there are some different implementations, should be tested by jmh
        // return new SimpleGrantedAuthority(role);
        // return new SimpleGrantedAuthority(role.intern());
        // return pool.computeIfAbsent(role, k -> new SimpleGrantedAuthority(k))
        // return pool.computeIfAbsent(role, k -> new SimpleGrantedAuthority(k.intern()))
        return pool.computeIfAbsent(role.intern(), SimpleGrantedAuthority::new);
    }

    @Override
    public String getAuthority() {
        return this.role;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof SimpleGrantedAuthority sga) {
            return this.role.equals(sga.getAuthority());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.role.hashCode();
    }

    @Override
    public String toString() {
        return this.role;
    }
}
