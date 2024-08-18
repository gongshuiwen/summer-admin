package com.hzboiler.erp.core.security.authorization;

import org.springframework.security.core.GrantedAuthority;

import java.io.Serial;

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

    private final String role;

    public SimpleGrantedAuthority(String role) {
        if (role == null || role.isBlank())
            throw new IllegalArgumentException("Role string cannot be null or blank");
        this.role = role;
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
