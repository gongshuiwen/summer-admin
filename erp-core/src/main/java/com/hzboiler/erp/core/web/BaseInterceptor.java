package com.hzboiler.erp.core.web;

import com.hzboiler.erp.core.context.BaseContextHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * @author gongshuiwen
 */
public final class BaseInterceptor implements HandlerInterceptor {

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        BaseContextHolder.clearContext();
    }
}
