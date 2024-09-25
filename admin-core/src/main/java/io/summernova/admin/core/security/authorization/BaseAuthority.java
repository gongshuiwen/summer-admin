package io.summernova.admin.core.security.authorization;

import java.io.Serializable;

/**
 * @author gongshuiwen
 */
public interface BaseAuthority extends Serializable {

    String getAuthority();
}
