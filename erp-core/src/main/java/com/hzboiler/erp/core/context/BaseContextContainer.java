package com.hzboiler.erp.core.context;

/**
 * Interface that provides get method for the current {@link BaseContext}.
 *
 * @author gongshuiwen
 */
public interface BaseContextContainer {

    /**
     * Get current {@link BaseContext}
     *
     * @return {@link BaseContext}
     */
    default BaseContext getContext() {
        return BaseContextHolder.getContext();
    }
}
