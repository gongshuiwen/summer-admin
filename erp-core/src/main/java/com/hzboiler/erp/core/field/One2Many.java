package com.hzboiler.erp.core.field;

import com.hzboiler.erp.core.field.annotations.InverseField;
import com.hzboiler.erp.core.field.util.RelationFieldUtil;
import com.hzboiler.erp.core.util.ReflectUtil;
import com.hzboiler.erp.core.model.BaseModel;
import lombok.Getter;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author gongshuiwen
 */
@Getter
public class One2Many<T extends BaseModel> {

    private static final Map<Field, Field> fieldInverseFieldCache = new ConcurrentHashMap<>();

    private List<Command<T>> commands; // for update use
    private List<T> values;

    public static Field getInverseField(Field field) {
        Field inverseField = fieldInverseFieldCache.getOrDefault(field, null);
        if (inverseField == null) {
            inverseField = _getInverseField(field);
            fieldInverseFieldCache.put(field, inverseField);
        }
        return inverseField;
    }

    private static Field _getInverseField(Field field) {
        Class<?> targetClass = RelationFieldUtil.getTargetModelClass(field);
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

    public static <T extends BaseModel> One2Many<T> ofCommands(List<Command<T>> commands) {
        One2Many<T> One2Many = new One2Many<>();
        One2Many.commands = commands;
        return One2Many;
    }

    public static <T extends BaseModel> One2Many<T> ofValues(List<T> values) {
        One2Many<T> One2Many = new One2Many<>();
        One2Many.values = values;
        return One2Many;
    }

    public List<T> get() {
        return values;
    }
}
