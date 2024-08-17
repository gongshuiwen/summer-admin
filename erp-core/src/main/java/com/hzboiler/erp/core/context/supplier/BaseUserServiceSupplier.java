package com.hzboiler.erp.core.context.supplier;

import com.hzboiler.erp.core.security.account.BaseUserService;

import java.util.function.Supplier;

/**
 * @author gongshuiwen
 */
public interface BaseUserServiceSupplier extends Supplier<BaseUserService> {

    static BaseUserService getBaseUserService() {
        return new SpringContextBaseUserServiceSupplier().get();
    }
}
