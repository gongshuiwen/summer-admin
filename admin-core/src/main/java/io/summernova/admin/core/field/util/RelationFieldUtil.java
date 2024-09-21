package io.summernova.admin.core.field.util;

import io.summernova.admin.common.util.ReflectUtil;
import io.summernova.admin.core.field.Many2Many;
import io.summernova.admin.core.field.Many2One;
import io.summernova.admin.core.field.One2Many;
import io.summernova.admin.core.field.annotations.One2ManyField;
import io.summernova.admin.core.model.BaseModel;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Static utility class which supports methods for relation field of {@link Many2One}, {@link One2Many} and {@link Many2Many}.
 *
 * @author gongshuiwen
 * @see Many2One
 * @see One2Many
 * @see Many2Many
 * @see One2ManyField
 */
public final class RelationFieldUtil {

    // caches for relation fields of model class
    private static final Map<Class<? extends BaseModel>, Field[]> many2OneFieldsCache = new ConcurrentHashMap<>();
    private static final Map<Class<? extends BaseModel>, Field[]> one2ManyFieldsCache = new ConcurrentHashMap<>();
    private static final Map<Class<? extends BaseModel>, Field[]> many2ManyFieldsCache = new ConcurrentHashMap<>();

    // cache for target model class of relation field
    private static final Map<ModelFieldKey, Class<? extends BaseModel>> targetClassCache = new ConcurrentHashMap<>();

    // cache for inverse field of x2many field
    private static final Map<ModelFieldKey, Field> inverseFieldCache = new ConcurrentHashMap<>();

    // prevent instantiation
    private RelationFieldUtil() {
    }

    /**
     * Determine whether a field is a relation field.
     */
    private static boolean isRelationField(Field field) {
        Type type = field.getType();
        return type == Many2One.class || type == Many2Many.class || type == One2Many.class;
    }

    /**
     * Get all declared (include inherited) {@link Many2One} fields of model class, cached by ConcurrentHashMap.
     */
    public static Field[] getMany2OneFields(Class<? extends BaseModel> modelClass) {
        return many2OneFieldsCache.computeIfAbsent(modelClass, (clazz) ->
                RelationFieldUtil._getRelationFields(clazz, Many2One.class));
    }

    /**
     * Get all declared (include inherited) {@link One2Many} fields of model class, cached by ConcurrentHashMap.
     */
    public static Field[] getOne2ManyFields(Class<? extends BaseModel> modelClass) {
        return one2ManyFieldsCache.computeIfAbsent(modelClass, (clazz) ->
                RelationFieldUtil._getRelationFields(clazz, One2Many.class));
    }

    /**
     * Get all declared (include inherited) {@link Many2Many} fields of model class, cached by ConcurrentHashMap.
     */
    public static Field[] getMany2ManyFields(Class<? extends BaseModel> modelClass) {
        return many2ManyFieldsCache.computeIfAbsent(modelClass, (clazz) ->
                RelationFieldUtil._getRelationFields(clazz, Many2Many.class));
    }

    /**
     * Real implement of getting all declared (include inherited) relation fields of model class.
     */
    private static Field[] _getRelationFields(Class<? extends BaseModel> modelClass, Class<?> fieldType) {
        Field[] fields = ReflectUtil.getAllDeclaredFieldsWithType(modelClass, fieldType);
        for (Field field : fields) field.setAccessible(true); // guarantee field is always accessible
        return fields;
    }

    /**
     * Get the target model class of relation field, cached by ConcurrentHashMap.
     *
     * @param modelClass the model class
     * @param field      the relation field, can be the field of the super class of modelClass
     * @return target model class of relation field
     */
    @SuppressWarnings("unchecked")
    public static <T extends BaseModel> Class<T> getTargetModelClass(Class<? extends BaseModel> modelClass, Field field) {
        ModelFieldKey modelFieldKey = new ModelFieldKey(modelClass, field);
        return (Class<T>) targetClassCache.computeIfAbsent(modelFieldKey, RelationFieldUtil::_getTargetModelClass);
    }

    /**
     * Real implement of getting the target model class of relation field.
     *
     * @param modelFieldKey key
     * @return target model class of relation field
     */
    private static Class<? extends BaseModel> _getTargetModelClass(ModelFieldKey modelFieldKey) {
        Class<? extends BaseModel> modelClass = modelFieldKey.modelClass();
        Field field = modelFieldKey.field();

        if (!isRelationField(field))
            throw new IllegalArgumentException("Field '" + _formatFieldName(field) + "' is not a relation field.");

        Type genericType = field.getGenericType();
        if (genericType instanceof ParameterizedType pt) {
            Type[] actualTypes = pt.getActualTypeArguments();
            if (actualTypes.length >= 1) {
                if (actualTypes[0] instanceof Class) {
                    @SuppressWarnings("unchecked")
                    Class<? extends BaseModel> targetClass = (Class<? extends BaseModel>) actualTypes[0];
                    return targetClass;
                } else if (actualTypes[0] instanceof TypeVariable) {
                    @SuppressWarnings("unchecked")
                    TypeVariable<Class<?>> typeVariable = (TypeVariable<Class<?>>) actualTypes[0];
                    @SuppressWarnings("unchecked")
                    Class<? extends BaseModel> targetClass = (Class<? extends BaseModel>) resolveActualTypeOfTypeVariable(modelClass, typeVariable);
                    if (targetClass != null) return targetClass;
                }
            }
        }

        throw new RuntimeException("Cannot find target class for relation field '" + _formatFieldName(field) + "'.");
    }

    private static Class<?> resolveActualTypeOfTypeVariable(Class<?> subClass, TypeVariable<Class<?>> typeVariable) {
        Class<?> superClass = typeVariable.getGenericDeclaration();
        Type genericSuperclass = subClass.getGenericSuperclass();
        if (genericSuperclass instanceof ParameterizedType pt && pt.getRawType() == superClass) {
            TypeVariable<? extends Class<?>>[] typeVariables = superClass.getTypeParameters();
            for (int i = 0; i < typeVariables.length; i++) {
                if (typeVariables[i].equals(typeVariable)) {
                    Type actualType = pt.getActualTypeArguments()[i];
                    if (actualType instanceof Class) return (Class<?>) actualType;
                }
            }
        }
        return null;
    }

    /**
     * Get inverse field of relation field, cached by ConcurrentHashMap.
     *
     * @param modelClass the model class
     * @param field      the relation field, can be the field of the super class of modelClass
     * @return the inverse field
     * @throws RuntimeException InverseField annotation not exist
     * @throws RuntimeException inverse field not exist
     */
    public static Field getInverseField(Class<? extends BaseModel> modelClass, Field field) {
        ModelFieldKey modelFieldKey = new ModelFieldKey(modelClass, field);
        return inverseFieldCache.computeIfAbsent(modelFieldKey, RelationFieldUtil::_getInverseField);
    }

    /**
     * Real implement of getting inverse field of relation field.
     */
    private static Field _getInverseField(ModelFieldKey modelFieldKey) {
        Class<? extends BaseModel> modelClass = modelFieldKey.modelClass();
        Field field = modelFieldKey.field();

        One2ManyField inverseFieldAnnotation = field.getDeclaredAnnotation(One2ManyField.class);
        if (inverseFieldAnnotation == null) {
            throw new RuntimeException("Cannot get annotation @One2ManyField for field '" + _formatFieldName(field) + "'.");
        }

        String inverseFieldName = inverseFieldAnnotation.inverseField();
        Class<?> targetClass = RelationFieldUtil.getTargetModelClass(modelClass, field);
        Field inverseField = ReflectUtil.getField(targetClass, inverseFieldName);
        if (inverseField == null) {
            throw new RuntimeException("The inverse field '" + inverseFieldName + "' of '" + _formatFieldName(field) + "'doesn't exist.");
        }
        inverseField.setAccessible(true);
        return inverseField;
    }

    private static String _formatFieldName(Field field) {
        return field.getDeclaringClass().getName() + '.' + field.getName();
    }

    // model class and field key of relation field, used as cache key of target model
    private record ModelFieldKey(Class<? extends BaseModel> modelClass, Field field) {
    }
}
