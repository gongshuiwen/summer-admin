package com.hzboiler.erp.core.field;

import com.hzboiler.erp.core.model.BaseModel;
import com.hzboiler.erp.core.util.ReflectUtil;
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
public class Many2One<T extends BaseModel> {

    private Long id;
    private String name;
    private T value;

    public static <T extends BaseModel> Many2One<T> ofId(Long id) {
        Many2One<T> many2One = new Many2One<>();
        many2One.id = id;
        return many2One;
    }

    public static <T extends BaseModel> Many2One<T> ofValue(T value) {
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
