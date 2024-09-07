package io.summernova.admin.core.security.account;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Set;

/**
 * @author gongshuiwen
 */
public interface BaseUser extends Serializable, UserDetails {

    /**
     * Get unique user id.
     */
    Long getId();

    @Override
    Set<? extends GrantedAuthority> getAuthorities();
}
