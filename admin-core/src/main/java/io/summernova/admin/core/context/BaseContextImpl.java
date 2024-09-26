package io.summernova.admin.core.context;

import io.summernova.admin.core.context.supplier.*;
import io.summernova.admin.core.security.account.BaseUser;
import io.summernova.admin.core.security.account.BaseUserService;
import io.summernova.admin.core.security.authorization.BaseAuthoritiesService;
import io.summernova.admin.core.security.authorization.BaseAuthority;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.*;

import static io.summernova.admin.core.security.Constants.GRANTED_AUTHORITY_ROLE_SYS_ADMIN;

/**
 * The default implementation of {@link BaseContext}.
 *
 * @author gongshuiwen
 */
public class BaseContextImpl implements BaseContext {

    @Getter
    private final String id;

    @Getter
    private final Long userId;

    // cache fields
    private BaseUser user;
    private Set<? extends BaseAuthority> authorities;
    private Boolean admin;

    private Map<String, Object> attributes;

    private SqlSession sqlSession;
    private HttpServletRequest request;

    public BaseContextImpl(Long userId) {
        this.id = UUID.randomUUID().toString();
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
    public Set<? extends BaseAuthority> getAuthorities() {
        if (authorities == null) {
            user = getUser();
            if (user != null) {
                user.getAuthorities();
            } else {
                BaseAuthoritiesService grantedAuthoritiesService = BaseAuthoritiesServiceSupplier.getGrantedAuthoritiesService();
                // Get authorities by GrantedAuthoritiesService, always wrapped with unmodifiable set
                authorities = Collections.unmodifiableSet(grantedAuthoritiesService.loadAuthoritiesByUserId(userId));
            }
        }
        return authorities;
    }

    @Override
    public boolean isAnonymous() {
        return userId == 0L;
    }

    @Override
    public boolean isSuperAdmin() {
        return userId == 1L;
    }

    @Override
    public boolean isAdmin() {
        if (admin == null) {
            Set<? extends BaseAuthority> authorities = getAuthorities();
            admin = authorities != null && authorities.contains(GRANTED_AUTHORITY_ROLE_SYS_ADMIN);
        }
        return admin;
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

    @Override
    public SqlSessionFactory getSqlSessionFactory() {
        return SqlSessionFactorySupplier.getSqlSessionFactory();
    }

    @Override
    public SqlSession getSqlSession() {
        if (sqlSession == null) {
            sqlSession = SqlSessionSupplier.getSqlSession();
        }
        return sqlSession;
    }

    @Override
    public HttpServletRequest getHttpServletRequest() {
        if (request == null) {
            request = HttpServletRequestSupplier.getHttpServletRequest();
        }
        return request;
    }
}
