package com.hzboiler.erp.core.service;

import com.hzboiler.erp.core.model.BaseModel;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author gongshuiwen
 */
public class BaseServiceRegistry {

    private static final Map<Class<? extends BaseModel>, BaseService<? extends BaseModel>>
            baseServiceRegistry = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    static <T extends BaseModel> BaseService<T> getService(Class<T> modelClass) {
        return (BaseService<T>) baseServiceRegistry.get(modelClass);
    }

    static <T extends BaseModel> void register(BaseService<T> baseService) {
        Objects.requireNonNull(baseService, "baseService must not be null");
        Objects.requireNonNull(baseService.getModelClass(), "modelClass of baseService must not be null");
        baseServiceRegistry.put(baseService.getModelClass(), baseService);
    }
}
