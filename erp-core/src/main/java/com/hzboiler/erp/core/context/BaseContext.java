package com.hzboiler.erp.core.context;

import com.hzboiler.erp.core.model.BaseUser;
import com.hzboiler.erp.core.security.GrantedAuthoritiesService;
import com.hzboiler.erp.core.security.BaseUserService;
import com.hzboiler.erp.core.util.SpringContextUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.*;

import static com.hzboiler.erp.core.security.Constants.GRANTED_AUTHORITY_ROLE_SYS_ADMIN;

/**
 * BaseContext stores common data for every request of the application,
 * it is created when required, discarded after each request and never shared between threads and requests.
 * @author gongshuiwen
 */
public final class BaseContext {

    private static final Set<? extends GrantedAuthority> EMPTY_AUTHORITIES = Set.of();

    @Getter
    private final Long userId;

    /**
     * Basic infos, will be lazily loaded by their get methods
     */
    private BaseUser user;
    private Set<? extends GrantedAuthority> authorities;
    private Boolean admin;

    // Attributes
    private Map<String, Object> attributes;

    // HttpServletRequest
    private HttpServletRequest request;

    public BaseContext(Long userId) {
        this.userId = userId;
    }

    /**
     * Retrieves the user information of the current user. If the user information is already cached, it is returned.
     * Otherwise, the user information is obtained from the security context.
     *
     * @return the user information or null if it is not available
     */
    @SuppressWarnings("unchecked")
    public <T extends BaseUser> T getUser() {
        if (user != null) {
            return (T) user;
        }

        if (userId == null) {
            return null;
        }

        // Get user info by BaseUserService
        BaseUserService userService = SpringContextUtil.getBean(BaseUserService.class);
        user = userService.selectById(userId);
        user.setAuthorities(getAuthorities());
        return (T) user;
    }

    /**
     * Retrieves the authorities of the current user. If the authorities are already cached, they are returned.
     * Otherwise, the authorities are obtained from the security context.
     *
     * @return an unmodifiable set of authorities or an empty set if they are not available
     */
    public Set<? extends GrantedAuthority> getAuthorities() {
        if (authorities == null) {
            authorities = _getAuthorities();
        }
        return authorities;
    }

    private Set<? extends GrantedAuthority> _getAuthorities() {
        // Get authorities by GrantedAuthoritiesService
        GrantedAuthoritiesService grantedAuthoritiesService = SpringContextUtil.getBean(GrantedAuthoritiesService.class);
        return Collections.unmodifiableSet(grantedAuthoritiesService.getAuthoritiesByUserId(getUserId()));
    }

    /**
     * Checks if the current user is an admin by retrieving their authorities and checking if they contain the
     * GRANTED_AUTHORITY_ROLE_SYS_ADMIN authority.
     *
     * @return true if the user is an admin, false otherwise.
     */
    public boolean isAdmin() {
        if (admin == null) {
            Set<? extends GrantedAuthority> authorities = getAuthorities();
            admin = authorities != null && authorities.contains(GRANTED_AUTHORITY_ROLE_SYS_ADMIN);
        }
        return admin;
    }

    /**
     * Retrieves the value associated with the given key from the attributes map.
     *
     * @param  key  the key whose associated value is to be returned
     * @return      the value to which the specified key is mapped, or null if the map contains no mapping for the key
     */
    public Object getAttribute(String key) {
        if (attributes == null) {
            return null;
        }
        return attributes.get(key);
    }

    /**
     * Sets an attribute in the attributes map with the given key and value. If the attributes map is null, it is
     * initialized as a new HashMap. Returns the previous value associated with the key, or null if there was no
     * mapping for the key.
     *
     * @param  key   the key with which the specified value is to be set
     * @param  value the value to be set
     * @return       the previous value associated with the key, or null if there was no mapping for the key
     */
    public Object setAttribute(String key, Object value) {
        if (attributes == null) {
            attributes = new HashMap<>();
        }
        return attributes.put(key, value);
    }

    /**
     * Removes the value associated with the given key from the attributes map.
     *
     * @param  key  the key whose associated value is to be removed
     * @return      the value that was associated with the key, or null if the map contained no mapping for the key
     */
    public Object removeAttribute(String key) {
        if (attributes == null) {
            return null;
        }
        return attributes.remove(key);
    }

    /**
     * Retrieves the attributes map. If the attributes map is null, a new HashMap is created and assigned to the attributes field.
     *
     * @return  the attributes map
     */
    public Map<String, Object> getAttributes() {
        if (attributes == null) {
            attributes = new HashMap<>();
        }
        return attributes;
    }

    /**
     * Retrieves the {@link jakarta.servlet.http.HttpServletRequest} associated with the current thread's request context.
     * If the request is already cached, it is returned directly. Otherwise, the request is obtained
     * from the {@link RequestContextHolder} and cached for future use.
     *
     * @return the {@link jakarta.servlet.http.HttpServletRequest} associated with the current thread's request context
     */
    public HttpServletRequest getRequest() {
        if (request == null) {
            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            if (requestAttributes instanceof ServletRequestAttributes servletRequestAttributes) {
                request = servletRequestAttributes.getRequest();
            } else {
                throw new RuntimeException("Failed to get HttpServletRequest from RequestContextHolder");
            }
        }
        return request;
    }
}
