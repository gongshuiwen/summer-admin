package com.hzboiler.core.service;

import com.hzboiler.core.model.BaseModel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author gongshuiwen
 */
public class BaseServiceRegistry {

    private static final Map<Class<?>, BaseService<?>> baseServiceRegistry = new ConcurrentHashMap<>();

    public static void register(BaseService<?> baseService) {
        if (baseService.getEntityClass() == null) {
            throw new RuntimeException("The entityClass of " + baseService + " shouldn't be null!");
        }
        baseServiceRegistry.put(baseService.getEntityClass(), baseService);
    }

    @SuppressWarnings("unchecked")
    public static <T extends BaseModel> BaseService<T> getService(Class<T> entityClass) {
        return (BaseService<T>) baseServiceRegistry.get(entityClass);
    }
}
