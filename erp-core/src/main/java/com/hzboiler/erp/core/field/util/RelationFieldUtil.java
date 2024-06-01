package com.hzboiler.erp.core.field.util;

import com.hzboiler.erp.core.field.Many2Many;
import com.hzboiler.erp.core.field.Many2One;
import com.hzboiler.erp.core.field.One2Many;
import com.hzboiler.erp.core.model.BaseModel;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Static utility class which supports methods for relation field Many2One, One2Many and Many2Many.
 *
 * @author gongshuiwen
 */
public abstract class RelationFieldUtil {

    // cache for target model class of relation field
    private static final Map<Field, Class<? extends BaseModel>> targetClassCache = new ConcurrentHashMap<>();

    /**
     * Get the target model class of relation field, cached by ConcurrentHashMap.
     *
     * @param field relation field
     * @return target model class of relation field
     */
    @SuppressWarnings("unchecked")
    public static <T extends BaseModel> Class<T> getTargetModelClass(Field field) {
        return (Class<T>) targetClassCache.computeIfAbsent(field, RelationFieldUtil::_getTargetModelClass);
    }

    /**
     * Real implement of getting the target model class of relation field.
     *
     * @param field relation field
     * @return target model class of relation field
     */
    private static <T extends BaseModel> Class<T> _getTargetModelClass(Field field) {
        if (!isRelationField(field))
            throw new IllegalArgumentException("Field " + field + " is not a relation field");

        Type genericType = field.getGenericType();
        if (genericType instanceof ParameterizedType pt) {
            Type[] actualTypes = pt.getActualTypeArguments();
            if (actualTypes.length == 1 && actualTypes[0] instanceof Class) {
                @SuppressWarnings("unchecked")
                Class<T> targetClass = (Class<T>) actualTypes[0];
                return targetClass;
            }
        }

        throw new RuntimeException("Cannot find target class for relation field " + field);
    }

    /**
     * Determine whether a field is a relation field.
     */
    private static boolean isRelationField(Field field) {
        Type type = field.getType();
        return type == Many2One.class || type == Many2Many.class || type == One2Many.class;
    }
}
