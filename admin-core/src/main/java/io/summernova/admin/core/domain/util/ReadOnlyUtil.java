package io.summernova.admin.core.domain.util;

import io.summernova.admin.common.util.ReflectUtil;
import io.summernova.admin.core.domain.annotations.ReadOnly;
import io.summernova.admin.core.domain.model.BaseModel;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Static utility class which supports methods for read-only field.
 *
 * @author gongshuiwen
 * @see ReadOnly
 */
public final class ReadOnlyUtil {

    // cache for read-only fields of model class
    private static final Map<Class<? extends BaseModel>, Field[]> readOnlyFieldsCache = new ConcurrentHashMap<>();

    // prevent instantiation
    private ReadOnlyUtil() {
    }

    /**
     * Get all declared fields (include inherited) with annotation {@link ReadOnly} of model class,
     * cache by ConcurrentHashMap.
     *
     * @param modelClass the model class
     * @return fields array, ensure all fields are always accessible
     */
    public static Field[] getReadOnlyFields(Class<? extends BaseModel> modelClass) {
        return readOnlyFieldsCache.computeIfAbsent(modelClass, ReadOnlyUtil::_getReadOnlyFields);
    }

    /**
     * Real implement of getting all declared fields (include inherited) with annotation {@link ReadOnly} of model class.
     */
    private static Field[] _getReadOnlyFields(Class<? extends BaseModel> modelClass) {
        Field[] fields = ReflectUtil.getAllDeclaredFieldsWithAnnotation(modelClass, ReadOnly.class);
        for (Field field : fields) field.setAccessible(true); // guarantee field is always accessible
        return fields;
    }
}
