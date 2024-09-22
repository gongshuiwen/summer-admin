package io.summernova.admin.core.context.supplier;

import io.summernova.admin.core.security.authorization.BaseAuthoritiesService;
import io.summernova.admin.core.util.SpringContextUtil;

/**
 * @author gongshuiwen
 */
public class SpringContextBaseAuthoritiesServiceSupplier implements BaseAuthoritiesServiceSupplier {
    @Override
    public BaseAuthoritiesService get() {
        return SpringContextUtil.getBean(BaseAuthoritiesService.class);
    }
}
