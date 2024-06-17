package com.hzboiler.erp.core.method.util;

import com.hzboiler.erp.core.method.annotations.Public;
import com.hzboiler.erp.core.service.BaseService;
import org.springframework.aop.framework.AopProxyUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author gongshuiwen
 */
public final class MethodUtil {

    // method cache
    private static final Map<MethodCacheKey, Method> methodCache = new ConcurrentHashMap<>();

    // prevent instantiation
    private MethodUtil() {
    }

    /**
     * Get public method for rpc call from service bean.
     *
     * @param service bean extend {@link BaseService}
     * @param methodName method name
     * @param paramCount method param count
     * @return {@link Method} instance
     */
    public static Method getPublicMethod(BaseService<?> service, String methodName, int paramCount) {
        MethodCacheKey cacheKey = new MethodCacheKey(service, methodName, paramCount);
        return methodCache.computeIfAbsent(cacheKey, k -> _getPublicMethod(service, methodName, paramCount));
    }

    private static Method _getPublicMethod(BaseService<?> service, String methodName, int paramCount) {
        Class<?> proxyClass = service.getClass();
        Class<?> sourceClass = AopProxyUtils.ultimateTargetClass(service);
        Method[] proxyMethods = proxyClass.getMethods();
        for (Method proxyMethod : proxyMethods) {
            if (proxyMethod.getName().equals(methodName) && proxyMethod.getParameterCount() == paramCount) {
                try {
                    // Check the source method in source class having annotation @Public and public flag
                    Method sourceMethod = sourceClass.getMethod(proxyMethod.getName(), proxyMethod.getParameterTypes());
                    if (!Modifier.isPublic(sourceMethod.getModifiers()) || sourceMethod.getDeclaredAnnotation(Public.class) == null)
                        throw new RuntimeException("Method '" + methodName + "' of '" + sourceClass.getName() + "' is not public.");
                } catch (NoSuchMethodException e) {
                    break;
                }
                return proxyMethod;
            }
        }

        throw new RuntimeException("Method '" + methodName + "' of '" + sourceClass.getName() + "' not found.");
    }

    private record MethodCacheKey(BaseService<?> service, String methodName, int paramCount) {
    }
}
