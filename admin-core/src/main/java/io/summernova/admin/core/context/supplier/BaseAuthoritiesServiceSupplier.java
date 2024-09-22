package io.summernova.admin.core.context.supplier;

import io.summernova.admin.core.security.authorization.BaseAuthoritiesService;

import java.util.function.Supplier;

/**
 * @author gongshuiwen
 */
public interface BaseAuthoritiesServiceSupplier extends Supplier<BaseAuthoritiesService> {

    static BaseAuthoritiesService getGrantedAuthoritiesService() {
        return new SpringContextBaseAuthoritiesServiceSupplier().get();
    }
}
