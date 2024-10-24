package io.summernova.admin.test.context;

import io.summernova.admin.core.context.BaseContextHolder;
import io.summernova.admin.core.security.authorization.BaseAuthority;
import io.summernova.admin.core.security.authorization.SimpleAuthority;
import io.summernova.admin.test.annotation.WithMockAdmin;
import io.summernova.admin.test.annotation.WithMockUser;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.HashSet;
import java.util.Set;

import static io.summernova.admin.core.security.Constants.GRANTED_AUTHORITY_ROLE_SYS_ADMIN;

/**
 * @author gongshuiwen
 */
public final class BaseContextExtension implements BeforeEachCallback, AfterEachCallback {

    @Override
    public void beforeEach(ExtensionContext context) {
        if (context.getRequiredTestMethod().isAnnotationPresent(WithMockUser.class)) {
            WithMockUser withMockUser = context.getRequiredTestMethod().getAnnotation(WithMockUser.class);
            MockBaseContextImpl baseContext = new MockBaseContextImpl(withMockUser.userId());

            // add authorities
            Set<BaseAuthority> authorities = new HashSet<>();
            for (String authority : withMockUser.authorities())
                authorities.add(SimpleAuthority.of(authority));
            for (String authority : withMockUser.roles())
                authorities.add(SimpleAuthority.of(authority));
            baseContext.setAuthorities(authorities);

            CustomBaseContextSupplier.setBaseContext(baseContext);
        } else if (context.getRequiredTestMethod().isAnnotationPresent(WithMockAdmin.class)) {
            MockBaseContextImpl baseContext = new MockBaseContextImpl(1L);
            baseContext.setAuthorities(Set.of(GRANTED_AUTHORITY_ROLE_SYS_ADMIN));
            CustomBaseContextSupplier.setBaseContext(baseContext);
        } else {
            CustomBaseContextSupplier.setBaseContext(new MockBaseContextImpl(0L));
        }
    }

    @Override
    public void afterEach(ExtensionContext context) {
        SqlSession sqlSession = BaseContextHolder.getContext().getSqlSession();
        sqlSession.rollback();
        sqlSession.close();
        BaseContextHolder.clearContext();
    }
}
