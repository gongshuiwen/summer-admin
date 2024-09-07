package io.summernova.admin.core.context.supplier;

import io.summernova.admin.core.security.account.BaseUserService;

import java.util.function.Supplier;

/**
 * @author gongshuiwen
 */
public interface BaseUserServiceSupplier extends Supplier<BaseUserService> {

    static BaseUserService getBaseUserService() {
        return new SpringContextBaseUserServiceSupplier().get();
    }
}
