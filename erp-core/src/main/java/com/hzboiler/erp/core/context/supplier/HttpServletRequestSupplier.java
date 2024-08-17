package com.hzboiler.erp.core.context.supplier;

import jakarta.servlet.http.HttpServletRequest;

import java.util.function.Supplier;

/**
 * @author gongshuiwen
 */
public interface HttpServletRequestSupplier extends Supplier<HttpServletRequest> {

    static HttpServletRequest getHttpServletRequest() {
        return new SpringWebHttpServletRequestSupplier().get();
    }
}
