package com.hzhg.plm.core.security;

import com.hzhg.plm.core.entity.BaseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class DataAccessAuthorityChecker {

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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new DataAccessException("没有权限访问该资源！");
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails user) {
            Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
            if (authorities == null || authorities.isEmpty()) {
                throw new DataAccessException("没有权限访问该资源！");
            }

            String requiredAuthority = entityClass.getSimpleName() + ":" + authority.name();
            if (authorities.contains(new SimpleGrantedAuthority(requiredAuthority))) {
                return;
            }
        }

        throw new DataAccessException("没有权限访问该资源！");
    }
}
