package io.summernova.admin.core.service;

import io.summernova.admin.core.model.BaseModel;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author gongshuiwen
 */
public class BaseServiceRegistry {

    private static final Map<String, BaseService<?>> baseServiceRegistryByModelName = new ConcurrentHashMap<>();

    static <T extends BaseModel> void register(BaseService<T> baseService) {
        Objects.requireNonNull(baseService, "baseService must not be null");
        Objects.requireNonNull(baseService.getModelClass(), "modelClass of baseService must not be null");
        baseServiceRegistryByModelName.put(getModelName(baseService.getModelClass()), baseService);
    }

    @SuppressWarnings("unchecked")
    public static <T extends BaseModel> BaseService<T> getByModelName(String modelName) {
        return (BaseService<T>) baseServiceRegistryByModelName.get(modelName);
    }

    @SuppressWarnings("unchecked")
    public static <T extends BaseModel> BaseService<T> getByModelClass(Class<T> modelClass) {
        return (BaseService<T>) baseServiceRegistryByModelName.get(getModelName(modelClass));
    }

    private static String getModelName(Class<?> modelClass) {
        String simpleName = modelClass.getSimpleName();
        return simpleName.substring(0, 1).toLowerCase() + simpleName.substring(1);
    }
}
