package io.summernova.admin.spring.context.supplier;

import io.summernova.admin.core.context.supplier.BaseUserServiceSupplier;
import io.summernova.admin.core.security.account.BaseUserService;
import io.summernova.admin.spring.util.SpringContextUtil;

/**
 * @author gongshuiwen
 */
public class SpringContextBaseUserServiceSupplier implements BaseUserServiceSupplier {
    @Override
    public BaseUserService get() {
        return SpringContextUtil.getBean(BaseUserService.class);
    }
}
