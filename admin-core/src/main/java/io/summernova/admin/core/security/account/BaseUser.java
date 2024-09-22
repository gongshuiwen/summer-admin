package io.summernova.admin.core.security.account;

import io.summernova.admin.core.security.authorization.BaseAuthority;
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

    Set<? extends BaseAuthority> getAuthorities();
}
