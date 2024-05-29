package com.hzboiler.erp.core.interceptor;

import com.hzboiler.erp.core.context.BaseContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * @author gongshuiwen
 */
public class BaseInterceptor implements HandlerInterceptor {

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        BaseContextHolder.clearContext();
    }
}