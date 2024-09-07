package io.summernova.admin.core.context.supplier;

import io.summernova.admin.core.context.BaseContext;
import io.summernova.admin.core.context.BaseContextImpl;
import io.summernova.admin.core.security.account.BaseUser;
import io.summernova.admin.core.util.SpringContextUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * @author gongshuiwen
 */
public class SpringSecurityBaseContextSupplier implements BaseContextSupplier {

    @Override
    public BaseContext get() {
        BaseContext baseContext = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            // normal case
            if (authentication.getPrincipal() instanceof BaseUser user) {
                baseContext = new BaseContextImpl(user.getId());
            }

            // for testing
            else if (authentication.getPrincipal() instanceof UserDetails userDetails) {
                userDetails = SpringContextUtil.getBean(UserDetailsService.class)
                        .loadUserByUsername(userDetails.getUsername());
                if (userDetails instanceof BaseUser baseUser)
                    baseContext = new BaseContextImpl(baseUser.getId());
            }
        }

        if (baseContext == null)
            baseContext = new BaseContextImpl(0L);

        return baseContext;
    }
}
