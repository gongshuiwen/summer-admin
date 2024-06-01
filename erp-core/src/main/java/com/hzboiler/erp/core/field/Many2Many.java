package com.hzboiler.erp.core.field;

import com.hzboiler.erp.core.util.ReflectUtil;
import com.hzboiler.erp.core.model.BaseModel;
import lombok.Getter;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author gongshuiwen
 */
@Getter
public class Many2Many<T extends BaseModel> {

    private static final Map<Class<?>, List<Field>> many2manyFieldsCache = new ConcurrentHashMap<>();

    private List<Command<T>> commands; // for update use
    private List<T> values;

    public static List<Field> getMany2ManyFields(Class<?> entityClass) {
        List<Field> fields = many2manyFieldsCache.getOrDefault(entityClass, null);
        if ( fields == null) {
            fields = Arrays.stream(ReflectUtil.getAllDeclaredFields(entityClass))
                    .filter(field -> field.getType() == Many2Many.class)
                    .peek(field -> field.setAccessible(true))
                    .collect(Collectors.toList());
            many2manyFieldsCache.put(entityClass, fields);
        }
        return fields;
    }

    public static <T extends BaseModel> Many2Many<T> ofCommands(List<Command<T>> commands) {
        Many2Many<T> many2Many = new Many2Many<>();
        many2Many.commands = commands;
        return many2Many;
    }

    public static <T extends BaseModel> Many2Many<T> ofValues(List<T> values) {
        Many2Many<T> many2Many = new Many2Many<>();
        many2Many.values = values;
        return many2Many;
    }

    public List<T> get() {
        return values;
    }
}
