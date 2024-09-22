package io.summernova.admin.core.security.authorization;

import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;

/**
 * @author gongshuiwen
 */
public interface BaseAuthority extends GrantedAuthority, Serializable {

    String getAuthority();
}
