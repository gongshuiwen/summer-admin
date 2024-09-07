package io.summernova.admin.core.web;

import io.summernova.admin.core.context.BaseContextHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * @author gongshuiwen
 */
public final class BaseInterceptor implements HandlerInterceptor {

    @Override
    public void afterCompletion(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                @NonNull Object handler, Exception ex) {
        BaseContextHolder.clearContext();
    }
}
