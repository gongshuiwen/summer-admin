package io.summernova.admin.spring.context.supplier;

import io.summernova.admin.core.context.supplier.HttpServletRequestSupplier;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @author gongshuiwen
 */
public class SpringWebHttpServletRequestSupplier implements HttpServletRequestSupplier {

    @Override
    public HttpServletRequest get() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes servletRequestAttributes) {
            return servletRequestAttributes.getRequest();
        } else {
            throw new RuntimeException("Failed to get HttpServletRequest from RequestContextHolder.");
        }
    }
}
