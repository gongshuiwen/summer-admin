package com.hzboiler.erp.core.security;

import com.hzboiler.erp.core.model.BaseModel;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * @author gongshuiwen
 */
public class DataAccessAuthorityChecker {

    /**
     * Check if the current user has the authority to access the specified model class with the given data access authority.
     * Throws a DataAccessException if the user does not have the required authority.
     *
     * @param modelClass The model class to check access for.
     * @param authority The data access authority required.
     * @throws DataAccessException If the user does not have the required authority.
     */
    public static void check(Class<? extends BaseModel> modelClass, DataAccessAuthority authority)
            throws DataAccessException {
        // SYS_ADMIN has access to everything
        if (GrantedAuthorityCheckUtils.isAdmin()) {
            return;
        }

        // Otherwise, check if the user has the required authority
        GrantedAuthority authorityRequired = new SimpleGrantedAuthority(modelClass.getSimpleName() + ":" + authority.name());
        if (GrantedAuthorityCheckUtils.contains(authorityRequired)) {
            return;
        }

        throw new DataAccessException("Data access denied: [" + authority.name() + "] " + modelClass.getSimpleName());
    }
}
