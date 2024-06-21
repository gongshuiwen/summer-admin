package com.hzboiler.erp.core.context;

/**
 * Interface that provides <code>getContext</code> method to get the current {@link BaseContext}.
 *
 * @author gongshuiwen
 * @see BaseContext
 * @see BaseContextHolder
 */
public interface BaseContextContainer {

    /**
     * Get the current {@link BaseContext}
     *
     * @return {@link BaseContext}
     */
    default BaseContext getContext() {
        return BaseContextHolder.getContext();
    }
}
