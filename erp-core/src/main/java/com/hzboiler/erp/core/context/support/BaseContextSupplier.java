package com.hzboiler.erp.core.context.support;

import com.hzboiler.erp.core.context.BaseContext;

import java.util.function.Supplier;

/**
 * @author gongshuiwen
 */
public interface BaseContextSupplier extends Supplier<BaseContext> {

    static BaseContext getBaseContext() {
        // use strategy pattern
        return new SpringSecurityBaseContextSupplier().get();
    }
}
