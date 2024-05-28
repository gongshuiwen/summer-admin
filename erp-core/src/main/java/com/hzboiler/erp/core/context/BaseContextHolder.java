package com.hzboiler.erp.core.context;

import com.hzboiler.erp.core.model.BaseUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * BaseContextHolder is utility class to get BaseContext instance,
 * and the BaseContext instance is lazily initialized and stored in ThreadLocal.
 *
 * @see BaseContext
 * @author gongshuiwen
 */
public class BaseContextHolder {

    private static final ThreadLocal<BaseContext> _local = new ThreadLocal<>();

    /**
     * Get the BaseContext of current thread.
     * If the BaseContext not exist yet, initialize and store it in ThreadLocal.
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
     * Create and initialize a new BaseContext instance.
     * The user info of the BaseContext is loaded from SecurityContext.
     */
    private static BaseContext initBaseContext() {
        BaseContext baseContext;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()
                && authentication.getPrincipal() instanceof BaseUser user) {
            baseContext = new BaseContext(user.getId());
        } else {
            baseContext = new BaseContext(0L);
        }
        return baseContext;
    }

    /**
     * Clear the BaseContext of current thread.
     * It should always be called when a request is finished.
     */
    public static void clearContext() {
        _local.remove();
    }

    // prevent instantiation
    private BaseContextHolder() {}
}
