package com.hzboiler.erp.core.field.util;

import com.hzboiler.erp.core.field.annotations.ReadOnly;
import com.hzboiler.erp.core.model.BaseModel;
import com.hzboiler.erp.core.util.ReflectUtil;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Static utility class which supports methods for read-only field.
 *
 * @see ReadOnly
 * @author gongshuiwen
 */
public final class ReadOnlyUtil {

    // cache for read-only fields of model class
    private static final Map<Class<? extends BaseModel>, Field[]> readOnlyFieldsCache = new ConcurrentHashMap<>();

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
