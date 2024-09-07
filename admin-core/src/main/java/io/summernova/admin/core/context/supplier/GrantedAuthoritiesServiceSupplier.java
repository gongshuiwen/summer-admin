package io.summernova.admin.core.context.supplier;

import io.summernova.admin.core.security.authorization.GrantedAuthoritiesService;

import java.util.function.Supplier;

/**
 * @author gongshuiwen
 */
public interface GrantedAuthoritiesServiceSupplier extends Supplier<GrantedAuthoritiesService> {

    static GrantedAuthoritiesService getGrantedAuthoritiesService() {
        return new SpringContextGrantedAuthoritiesServiceSupplier().get();
    }
}
