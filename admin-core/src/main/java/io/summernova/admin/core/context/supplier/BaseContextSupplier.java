package io.summernova.admin.core.context.supplier;

import io.summernova.admin.core.context.BaseContext;

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
