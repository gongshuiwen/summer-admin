package io.summernova.admin.core.context;

import io.summernova.admin.core.security.account.BaseUser;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.ibatis.session.SqlSession;
import org.springframework.security.core.GrantedAuthority;

import java.util.Map;
import java.util.Set;

/**
 * BaseContext stores common data for every request of the application.
 * It is created when required, discarded after each request and never shared between threads and requests.
 *
 * @author gongshuiwen
 */
public interface BaseContext {

    /**
     * Get the user ID of the current request.
     *
     * @return the user ID or null if it is not available
     */
    Long getUserId();

    /**
     * Get the user information of the current request.
     *
     * @return the user information or null if it is not available
     */
    <T extends BaseUser> T getUser();

    /**
     * Get the user authorities of the current request.
     *
     * @return an unmodifiable set of authorities or an empty set if they are not available
     */
    Set<? extends GrantedAuthority> getAuthorities();

    /**
     * Check if the current user is a super admin.
     *
     * @return true if the user is a super admin, false otherwise.
     */
    boolean isSuperAdmin();

    /**
     * Check if the current user is an admin.
     *
     * @return true if the user is an admin, false otherwise.
     */
    boolean isAdmin();

    /**
     * Get the {@link HttpServletRequest} instance of the current request.
     *
     * @return the {@link HttpServletRequest} instance of the current request
     */
    HttpServletRequest getHttpServletRequest();

    /**
     * Get the {@link SqlSession} of the current request.
     *
     * @return the {@link SqlSession} of the current request
     */
    SqlSession getSqlSession();

    /**
     * Get the attributes map of the current request.
     *
     * @return the attributes map of the current request
     */
    Map<String, Object> getAttributes();

    /**
     * Get the value mapped to the given key from the attributes map.
     *
     * @param key the key whose mapped value is to be retrieved
     * @return the value mapped to the given key, or null if there was no mapping for the key
     */
    Object getAttribute(String key);

    /**
     * Set the value with the given key in the attributes map.
     *
     * @param key   the key whose mapped value is to be set
     * @param value the value to be set
     * @return the previous value mapped to the given key, or null if there was no mapping for the key
     */
    Object setAttribute(String key, Object value);

    /**
     * Remove the given key from the attributes map.
     *
     * @param key the key whose mapped value is to be removed
     * @return the value mapped to the given key, or null if there was no mapping for the key
     */
    Object removeAttribute(String key);
}
