package com.hzboiler.core.fields.util;

import com.hzboiler.core.fields.annotations.ReadOnly;
import com.hzboiler.core.utils.ReflectUtil;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ReadOnlyUtil {

    private static final Map<Class<?>, Field[]> readOnlyFieldsCache = new ConcurrentHashMap<>();

    /**
     * Get all declared fields (include inherited) with annotation {@link com.hzboiler.core.fields.annotations.ReadOnly} for class ,
     * a ConcurrentHashMap cache is used.
     * @param clazz the class
     * @return fields array, ensure all fields are always accessible
     */
    public static Field[] getReadOnlyFields(Class<?> clazz) {
        return readOnlyFieldsCache.computeIfAbsent(clazz, ReadOnlyUtil::getReadOnlyFieldsInternal);
    }

    private static Field[] getReadOnlyFieldsInternal(Class<?> clazz) {
        Field[] fields = ReflectUtil.getAllDeclaredFieldsWithAnnotation(clazz, ReadOnly.class);
        for (Field field : fields) field.setAccessible(true);
        return fields;
    }
}
