package com.hzboiler.core.fields;

import com.hzboiler.core.fields.annotations.InverseField;
import com.hzboiler.core.entity.BaseEntity;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Getter
public class One2Many<T extends BaseEntity> {

    private static final Map<Class<?>, List<Field>> one2manyFieldsCache = new ConcurrentHashMap<>();
    private static final Map<Field, Class<?>> fieldTargetClassCache = new ConcurrentHashMap<>();
    private static final Map<Field, Field> fieldInverseFieldCache = new ConcurrentHashMap<>();

    private List<Command<T>> commands; // for update use
    private List<T> values;

    public static List<Field> getOne2ManyFields(Class<?> entityClass) {
        List<Field> fields = one2manyFieldsCache.getOrDefault(entityClass, null);
        if (fields == null) {
            fields = Arrays.stream(entityClass.getDeclaredFields())
                    .filter(field -> field.getType() == One2Many.class)
                    .peek(field -> field.setAccessible(true))
                    .collect(Collectors.toList());
            one2manyFieldsCache.put(entityClass, fields);
        }
        return fields;
    }

    public static Class<?> getTargetClass(Field field) {
        Class<?> targetClass = fieldTargetClassCache.getOrDefault(field, null);
        if (targetClass == null) {
            targetClass = _getTargetClass(field);
            fieldTargetClassCache.put(field, targetClass);
        }
        return targetClass;
    }

    @NotNull
    private static Class<?> _getTargetClass(Field field) {
        if (field.getType() != One2Many.class)
            throw new IllegalArgumentException("Not a One2Many field: " + field.getName());

        Type genericType = field.getGenericType();
        if (genericType instanceof ParameterizedType pt) {
            Type[] actualTypes = pt.getActualTypeArguments();
            if (actualTypes.length > 0 && actualTypes[0] instanceof Class<?> targetClass)
                return targetClass;
        }
        throw new RuntimeException("Cannot find target class for One2Many field: " + field.getName());
    }

    public static Field getInverseField(Field field) {
        Field inverseField = fieldInverseFieldCache.getOrDefault(field, null);
        if (inverseField == null) {
            inverseField = _getInverseField(field);
            fieldInverseFieldCache.put(field, inverseField);
        }
        return inverseField;
    }

    private static Field _getInverseField(Field field) {
        Class<?> targetClass = getTargetClass(field);
        InverseField inverseFieldAnnotation = field.getDeclaredAnnotation(InverseField.class);
        Field inverseField;
        try {
            inverseField = targetClass.getDeclaredField(inverseFieldAnnotation.value());
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        inverseField.setAccessible(true);
        return inverseField;
    }

    public static <T extends BaseEntity> One2Many<T> ofCommands(List<Command<T>> commands) {
        One2Many<T> One2Many = new One2Many<>();
        One2Many.commands = commands;
        return One2Many;
    }

    public static <T extends BaseEntity> One2Many<T> ofValues(List<T> values) {
        One2Many<T> One2Many = new One2Many<>();
        One2Many.values = values;
        return One2Many;
    }

    public List<T> get() {
        return values;
    }
}
