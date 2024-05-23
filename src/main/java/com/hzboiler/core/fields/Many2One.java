package com.hzboiler.core.fields;

import com.hzboiler.core.entity.BaseEntity;
import com.hzboiler.core.util.ReflectUtil;
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
public class Many2One<T extends BaseEntity> {

    private static final Map<Class<?>, List<Field>> many2oneFieldsCache = new ConcurrentHashMap<>();
    private static final Map<Field, Class<?>> fieldTargetClassCache = new ConcurrentHashMap<>();

    private Long id;
    private String name;
    private T value;

    public static List<Field> getMany2OneFields(Class<?> entityClass) {
        List<Field> fields = many2oneFieldsCache.getOrDefault(entityClass, null);
        if ( fields == null) {
            fields = Arrays.stream(ReflectUtil.getAllDeclaredFields(entityClass))
                    .filter(field -> field.getType() == Many2One.class)
                    .peek(field -> field.setAccessible(true))
                    .collect(Collectors.toList());
            many2oneFieldsCache.put(entityClass, fields);
        }
        return fields;
    }

    public static Class<?> getTargetClass(Field field) {
        Class<?> targetClass = fieldTargetClassCache.getOrDefault(field, null);
        if ( targetClass == null) {
            targetClass = _getTargetClass(field);
            fieldTargetClassCache.put(field, targetClass);
        }
        return targetClass;
    }

    @NotNull
    private static Class<?> _getTargetClass(Field field) {
        if (field.getType() != Many2One.class)
            throw new IllegalArgumentException("Not a Many2One field: " + field.getName());

        Type genericType = field.getGenericType();
        if (genericType instanceof ParameterizedType pt) {
            Type[] actualTypes = pt.getActualTypeArguments();
            if (actualTypes.length > 0 && actualTypes[0] instanceof Class<?> targetClass)
                return targetClass;
        }
        throw new RuntimeException("Cannot find target class for Many2One field: " + field.getName());
    }

    public static <T extends BaseEntity> Many2One<T> ofId(Long id) {
        Many2One<T> many2One = new Many2One<>();
        many2One.id = id;
        return many2One;
    }

    public static <T extends BaseEntity> Many2One<T> ofValue(T value) {
        Many2One<T> many2One = new Many2One<>();
        many2One.id = value.getId();
        many2One.name = value.getDisplayName();
        many2One.value = value;
        return many2One;
    }

    public T get() {
        return value;
    }

    public void set(T value) {
        this.value = value;
    }
}
