package io.summernova.admin.core.security.account;

import io.summernova.admin.core.security.authorization.BaseAuthority;

import java.io.Serializable;
import java.util.Set;

/**
 * @author gongshuiwen
 */
public interface BaseUser extends Serializable {

    /**
     * Get unique user id.
     */
    Long getId();

    Set<? extends BaseAuthority> getAuthorities();
}
