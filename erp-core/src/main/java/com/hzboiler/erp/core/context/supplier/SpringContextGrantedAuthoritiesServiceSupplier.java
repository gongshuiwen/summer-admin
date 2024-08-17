package com.hzboiler.erp.core.context.supplier;

import com.hzboiler.erp.core.security.authorization.GrantedAuthoritiesService;
import com.hzboiler.erp.core.util.SpringContextUtil;

/**
 * @author gongshuiwen
 */
public class SpringContextGrantedAuthoritiesServiceSupplier implements GrantedAuthoritiesServiceSupplier {
    @Override
    public GrantedAuthoritiesService get() {
        return SpringContextUtil.getBean(GrantedAuthoritiesService.class);
    }
}
