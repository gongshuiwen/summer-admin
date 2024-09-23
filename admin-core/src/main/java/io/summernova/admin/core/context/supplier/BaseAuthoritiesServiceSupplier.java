package io.summernova.admin.core.context.supplier;

import io.summernova.admin.core.security.authorization.BaseAuthoritiesService;

import java.util.function.Supplier;

/**
 * @author gongshuiwen
 */
public interface BaseAuthoritiesServiceSupplier extends Supplier<BaseAuthoritiesService> {

    BaseAuthoritiesServiceSupplier DEFAULT = ServiceLoaderUtil.getProvider(BaseAuthoritiesServiceSupplier.class);

    static BaseAuthoritiesService getGrantedAuthoritiesService() {
        return DEFAULT.get();
    }
}
