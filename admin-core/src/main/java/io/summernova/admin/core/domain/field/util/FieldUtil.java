package io.summernova.admin.core.domain.field.util;

import io.summernova.admin.common.util.ReflectUtil;
import io.summernova.admin.core.domain.model.BaseModel;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Static utility class which supports methods for field of {@link BaseModel}.
 *
 * @author gongshuiwen
 */
public final class FieldUtil {

    // field names cache
    private static final Map<Class<? extends BaseModel>, Set<String>> fieldNamesCache = new ConcurrentHashMap<>();

    // prevent external instantiation
    private FieldUtil() {
    }

    /**
     * Check if the field exists in the modelClass of {@link BaseModel}, include inherited fields.
     *
     * @param modelClass the model class
     * @param fieldName  the field name
     * @return true if the field exists, otherwise false
     */
    public static boolean checkFieldExists(Class<? extends BaseModel> modelClass, String fieldName) {
        Objects.requireNonNull(modelClass, "modelClass must not be null");
        return getFields(modelClass).contains(fieldName);
    }

    private static Set<String> getFields(Class<? extends BaseModel> modelClass) {
        return fieldNamesCache.computeIfAbsent(modelClass, c -> {
            Field[] declaredFields = ReflectUtil.getAllDeclaredFields(c);
            return Arrays.stream(declaredFields).map(Field::getName).collect(Collectors.toSet());
        });
    }
}
