package com.hzboiler.erp.core.field.util;

import com.hzboiler.erp.core.field.Many2Many;
import com.hzboiler.erp.core.field.Many2One;
import com.hzboiler.erp.core.field.One2Many;
import com.hzboiler.erp.core.model.BaseModel;
import com.hzboiler.erp.core.util.ReflectUtil;

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

    // caches for relation fields of model class
    private static final Map<Class<? extends BaseModel>, Field[]> many2OneFieldsCache = new ConcurrentHashMap<>();
    private static final Map<Class<? extends BaseModel>, Field[]> one2ManyFieldsCache = new ConcurrentHashMap<>();
    private static final Map<Class<? extends BaseModel>, Field[]> many2ManyFieldsCache = new ConcurrentHashMap<>();

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

    /**
     * Get Many2One fields of model class, cached by ConcurrentHashMap.
     */
    public static Field[] getMany2OneFields(Class<? extends BaseModel> modelClass) {
        return many2OneFieldsCache.computeIfAbsent(modelClass, (clazz) ->
                RelationFieldUtil._getRelationFields(clazz, Many2One.class));
    }

    /**
     * Get One2Many fields of model class, cached by ConcurrentHashMap.
     *
     */
    public static Field[] getOne2ManyFields(Class<? extends BaseModel> modelClass) {
        return one2ManyFieldsCache.computeIfAbsent(modelClass, (clazz) ->
                RelationFieldUtil._getRelationFields(clazz, One2Many.class));
    }

    /**
     * Get Many2Many fields of model class, cached by ConcurrentHashMap.
     */
    public static Field[] getMany2ManyFields(Class<? extends BaseModel> modelClass) {
        return many2ManyFieldsCache.computeIfAbsent(modelClass, (clazz) ->
                RelationFieldUtil._getRelationFields(clazz, Many2Many.class));
    }

    /**
     * Real implement of getting relation fields of model class.
     */
    private static Field[] _getRelationFields(Class<? extends BaseModel> modelClass, Class<?> fieldType) {
        Field[] fields = ReflectUtil.getAllDeclaredFieldsWithType(modelClass, fieldType);
        for (Field field : fields) field.setAccessible(true); // guarantee field is always accessible
        return fields;
    }
}
