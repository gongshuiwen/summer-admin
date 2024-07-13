package com.hzboiler.erp.core.context.support;

import com.hzboiler.erp.core.security.BaseUserService;

import java.util.function.Supplier;

/**
 * @author gongshuiwen
 */
public interface BaseUserServiceSupplier extends Supplier<BaseUserService> {

    static BaseUserService getBaseUserService() {
        return new SpringContextBaseUserServiceSupplier().get();
    }
}
