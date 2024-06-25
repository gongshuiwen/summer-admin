package com.hzboiler.erp.core.context;

import com.hzboiler.erp.core.model.BaseUser;
import com.hzboiler.erp.core.util.SpringContextUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * BaseContextHolder is an utility class to get {@link BaseContext} instance,
 * which is lazily initialized and stored in {@link ThreadLocal}.
 *
 * @author gongshuiwen
 * @see BaseContext
 */
public final class BaseContextHolder {

    // thread local for BaseContext
    private static final ThreadLocal<BaseContext> _local = new ThreadLocal<>();

    // prevent external instantiation
    private BaseContextHolder() {
    }

    /**
     * Get the {@link BaseContext} of current thread.
     * If the {@link BaseContext} not exist yet, initialize and store it in {@link ThreadLocal}.
     *
     * @return {@link BaseContext}
     */
    public static BaseContext getContext() {
        BaseContext baseContext = _local.get();
        if (baseContext == null) {
            baseContext = initBaseContext();
            _local.set(baseContext);
        }
        return baseContext;
    }

    /**
     * Create and initialize a new {@link BaseContext} instance.
     * The user info of the {@link BaseContext} is loaded from {@link SecurityContext}.
     */
    private static BaseContext initBaseContext() {
        BaseContext baseContext;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()
                && authentication.getPrincipal() instanceof BaseUser user) {
            baseContext = new BaseContext(user.getId());
        } else if (authentication != null && authentication.isAuthenticated()
                && authentication.getPrincipal() instanceof User user) {
            Long userId = ((BaseUser) SpringContextUtil.getBean(UserDetailsService.class)
                    .loadUserByUsername(user.getUsername())).getId();
            baseContext = new BaseContext(userId);
        } else {
            baseContext = new BaseContext(0L);
        }
        return baseContext;
    }

    /**
     * Clear the {@link BaseContext} of current thread.
     * It should always be called when a request is finished.
     */
    public static void clearContext() {
        _local.remove();
    }
}
