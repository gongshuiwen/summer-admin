package io.summernova.admin.core.context.supplier;

import io.summernova.admin.core.security.authorization.GrantedAuthoritiesService;
import io.summernova.admin.core.util.SpringContextUtil;

/**
 * @author gongshuiwen
 */
public class SpringContextGrantedAuthoritiesServiceSupplier implements GrantedAuthoritiesServiceSupplier {
    @Override
    public GrantedAuthoritiesService get() {
        return SpringContextUtil.getBean(GrantedAuthoritiesService.class);
    }
}
