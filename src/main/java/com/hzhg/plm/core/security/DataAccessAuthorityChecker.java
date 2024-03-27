package com.hzhg.plm.core.security;

import com.hzhg.plm.core.entity.BaseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

public class DataAccessAuthorityChecker {

    public static final String ROLE_PREFIX = "ROLE_";
    public static final String ROLE_ADMIN = "SYS_ADMIN";

    /**
     * Check if the current user has the authority to access the specified entity class with the given data access authority.
     * Throws a DataAccessException if the user does not have the required authority.
     *
     * @param entityClass The entity class to check access for.
     * @param authority The data access authority required.
     * @throws DataAccessException If the user does not have the required authority.
     */
    public static void check(Class<? extends BaseEntity> entityClass, DataAccessAuthority authority)
            throws DataAccessException {
        List<SimpleGrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority(ROLE_PREFIX + ROLE_ADMIN),
                new SimpleGrantedAuthority(entityClass.getSimpleName() + ":" + authority.name())
        );

        if (GrantedAuthorityCheckUtils.containsAny(authorities)) {
            return;
        }

        throw new DataAccessException("Data access authority check failed.");
    }
}
