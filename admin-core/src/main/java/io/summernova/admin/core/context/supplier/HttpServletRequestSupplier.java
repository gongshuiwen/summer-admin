package io.summernova.admin.core.context.supplier;

import jakarta.servlet.http.HttpServletRequest;

import java.util.function.Supplier;

/**
 * @author gongshuiwen
 */
public interface HttpServletRequestSupplier extends Supplier<HttpServletRequest> {

    HttpServletRequestSupplier DEFAULT = ServiceLoaderUtil.getProvider(HttpServletRequestSupplier.class);

    static HttpServletRequest getHttpServletRequest() {
        return DEFAULT.get();
    }
}
