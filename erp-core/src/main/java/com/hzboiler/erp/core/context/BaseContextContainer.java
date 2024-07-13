package com.hzboiler.erp.core.context;

/**
 * Interface that provides <code>getContext</code> method to get the {@link BaseContext} instance of the current request.
 *
 * @author gongshuiwen
 * @see BaseContext
 * @see BaseContextHolder
 */
public interface BaseContextContainer {

    /**
     * Get the {@link BaseContext} instance of the current request.
     *
     * @return {@link BaseContext} instance of the current request
     */
    default BaseContext getContext() {
        return BaseContextHolder.getContext();
    }
}
