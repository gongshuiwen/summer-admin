package io.summernova.admin.core.context;

import io.summernova.admin.core.context.supplier.BaseUserServiceSupplier;
import io.summernova.admin.core.context.supplier.GrantedAuthoritiesServiceSupplier;
import io.summernova.admin.core.context.supplier.HttpServletRequestSupplier;
import io.summernova.admin.core.context.supplier.SqlSessionSupplier;
import io.summernova.admin.core.security.account.BaseUser;
import io.summernova.admin.core.security.account.BaseUserService;
import io.summernova.admin.core.security.authorization.GrantedAuthoritiesService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import org.apache.ibatis.session.SqlSession;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static io.summernova.admin.core.security.Constants.GRANTED_AUTHORITY_ROLE_SYS_ADMIN;

/**
 * The default implementation of {@link BaseContext}.
 *
 * @author gongshuiwen
 */
public class BaseContextImpl implements BaseContext {

    @Getter
    private final Long userId;

    private BaseUser user;
    private Set<? extends GrantedAuthority> authorities;
    private Boolean superAdmin;
    private Boolean admin;

    private HttpServletRequest request;
    private Map<String, Object> attributes;

    public BaseContextImpl(Long userId) {
        this.userId = userId;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends BaseUser> T getUser() {
        if (user != null) {
            return (T) user;
        }

        if (userId == null) {
            return null;
        }

        // Get user info by BaseUserService
        BaseUserService userService = BaseUserServiceSupplier.getBaseUserService();
        user = userService.loadUserByUserId(userId);
        return (T) user;
    }

    @Override
    public Set<? extends GrantedAuthority> getAuthorities() {
        if (authorities == null) {
            user = getUser();
            if (user != null) {
                user.getAuthorities();
            } else {
                GrantedAuthoritiesService grantedAuthoritiesService = GrantedAuthoritiesServiceSupplier.getGrantedAuthoritiesService();
                // Get authorities by GrantedAuthoritiesService, always wrapped with unmodifiable set
                authorities = Collections.unmodifiableSet(grantedAuthoritiesService.getAuthoritiesByUserId(getUserId()));
            }
        }
        return authorities;
    }

    @Override
    public boolean isSuperAdmin() {
        if (superAdmin == null) {
            superAdmin = getUserId() == 1L;
        }
        return superAdmin;
    }

    @Override
    public boolean isAdmin() {
        if (admin == null) {
            Set<? extends GrantedAuthority> authorities = getAuthorities();
            admin = authorities != null && authorities.contains(GRANTED_AUTHORITY_ROLE_SYS_ADMIN);
        }
        return admin;
    }

    @Override
    public HttpServletRequest getHttpServletRequest() {
        if (request == null) {
            request = HttpServletRequestSupplier.getHttpServletRequest();
        }
        return request;
    }

    @Override
    public SqlSession getSqlSession() {
        return SqlSessionSupplier.getSqlSession();
    }

    @Override
    public Map<String, Object> getAttributes() {
        if (attributes == null) {
            attributes = new HashMap<>();
        }
        return attributes;
    }

    @Override
    public Object getAttribute(String key) {
        if (attributes == null) {
            return null;
        }
        return attributes.get(key);
    }

    @Override
    public Object setAttribute(String key, Object value) {
        if (attributes == null) {
            attributes = new HashMap<>();
        }
        return attributes.put(key, value);
    }

    @Override
    public Object removeAttribute(String key) {
        if (attributes == null) {
            return null;
        }
        return attributes.remove(key);
    }
}
