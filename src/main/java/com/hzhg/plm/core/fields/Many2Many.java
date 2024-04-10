package com.hzhg.plm.core.fields;

import com.hzhg.plm.core.entity.BaseEntity;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Getter
public class Many2Many<T extends BaseEntity> {

    private static final Map<Class<?>, List<Field>> many2manyFieldsCache = new ConcurrentHashMap<>();
    private static final Map<Field, Class<?>> fieldTargetClassCache = new ConcurrentHashMap<>();

    private List<Command<T>> commands; // for update use
    private List<T> values;

    public static List<Field> getMany2ManyFields(Class<?> entityClass) {
        List<Field> fields = many2manyFieldsCache.getOrDefault(entityClass, null);
        if ( fields == null) {
            fields = Arrays.stream(entityClass.getDeclaredFields())
                    .filter(field -> field.getType() == Many2Many.class)
                    .peek(field -> field.setAccessible(true))
                    .collect(Collectors.toList());
            many2manyFieldsCache.put(entityClass, fields);
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
        if (field.getType() != Many2Many.class)
            throw new IllegalArgumentException("Not a Many2Many field: " + field.getName());

        Type genericType = field.getGenericType();
        if (genericType instanceof ParameterizedType pt) {
            Type[] actualTypes = pt.getActualTypeArguments();
            if (actualTypes.length > 0 && actualTypes[0] instanceof Class<?> targetClass)
                return targetClass;
        }
        throw new RuntimeException("Cannot find target class for Many2Many field: " + field.getName());
    }

    public static <T extends BaseEntity> Many2Many<T> ofCommands(List<Command<T>> commands) {
        Many2Many<T> many2Many = new Many2Many<>();
        many2Many.commands = commands;
        return many2Many;
    }

    public static <T extends BaseEntity> Many2Many<T> ofValues(List<T> values) {
        Many2Many<T> many2Many = new Many2Many<>();
        many2Many.values = values;
        return many2Many;
    }

    public List<T> get() {
        return values;
    }
}
