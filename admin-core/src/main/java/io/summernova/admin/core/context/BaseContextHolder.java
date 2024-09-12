package io.summernova.admin.core.context;

import io.summernova.admin.core.context.supplier.BaseContextSupplier;

/**
 * A static utility class to get the {@link BaseContext} instance of current request environment,
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
     * Get the {@link BaseContext} instance of current request environment.
     * <p>
     * If the {@link BaseContext} not exist yet, initialize and store it to {@link ThreadLocal}.
     *
     * @return {@link BaseContext} instance
     */
    public static BaseContext getContext() {
        BaseContext baseContext = _local.get();
        if (baseContext == null)
            _local.set(baseContext = getBaseContext());
        return baseContext;
    }

    /**
     * Clear the {@link BaseContext} instance of current request environment.
     * <p>
     * Will be called when a request is finished.
     */
    public static void clearContext() {
        _local.remove();
    }

    private static BaseContext getBaseContext() {
        return BaseContextSupplier.getBaseContext();
    }
}
