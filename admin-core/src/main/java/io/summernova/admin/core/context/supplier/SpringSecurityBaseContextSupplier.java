package io.summernova.admin.core.context.supplier;

import io.summernova.admin.core.context.BaseContext;
import io.summernova.admin.core.context.BaseContextImpl;
import io.summernova.admin.core.security.account.BaseUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author gongshuiwen
 */
public class SpringSecurityBaseContextSupplier implements BaseContextSupplier {

    @Override
    public BaseContext get() {
        BaseContext baseContext = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            if (authentication.getPrincipal() instanceof BaseUser user) {
                baseContext = new BaseContextImpl(user.getId());
            }
        }

        if (baseContext == null)
            baseContext = new BaseContextImpl(0L);

        return baseContext;
    }
}
