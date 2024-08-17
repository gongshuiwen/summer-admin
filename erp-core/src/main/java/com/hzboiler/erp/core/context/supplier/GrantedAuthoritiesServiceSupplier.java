package com.hzboiler.erp.core.context.supplier;

import com.hzboiler.erp.core.security.authorization.GrantedAuthoritiesService;

import java.util.function.Supplier;

/**
 * @author gongshuiwen
 */
public interface GrantedAuthoritiesServiceSupplier extends Supplier<GrantedAuthoritiesService> {

    static GrantedAuthoritiesService getGrantedAuthoritiesService() {
        return new SpringContextGrantedAuthoritiesServiceSupplier().get();
    }
}
