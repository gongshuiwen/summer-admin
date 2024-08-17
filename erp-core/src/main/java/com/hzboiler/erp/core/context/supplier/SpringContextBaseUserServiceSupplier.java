package com.hzboiler.erp.core.context.supplier;

import com.hzboiler.erp.core.security.BaseUserService;
import com.hzboiler.erp.core.util.SpringContextUtil;

/**
 * @author gongshuiwen
 */
public class SpringContextBaseUserServiceSupplier implements BaseUserServiceSupplier {
    @Override
    public BaseUserService get() {
        return SpringContextUtil.getBean(BaseUserService.class);
    }
}
