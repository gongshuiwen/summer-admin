package com.hzboiler.erp.core.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Static utility class that contains reflection-related utility methods.
 *
 * @author gongshuiwen
 */
public abstract class ReflectUtil {

    /**
     * Get all declared fields (include inherited) of class
     * @param clazz the class
     * @return fields array
     */
    public static Field[] getAllDeclaredFields(Class<?> clazz) {
        List<Field> fieldList = new ArrayList<>();
        while (clazz != null){
            fieldList.addAll(List.of(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass();
        }
        return fieldList.toArray(new Field[0]);
    }

    /**
     * Get all declared fields (include inherited) with given annotation of class
     * @param clazz the class
     * @return fields array
     */
    public static Field[] getAllDeclaredFieldsWithAnnotation(Class<?> clazz, Class<? extends Annotation> annotation) {
        List<Field> fieldList = new ArrayList<>();
        while (clazz != null) {
            Arrays.stream(clazz.getDeclaredFields())
                    .filter(field -> field.isAnnotationPresent(annotation))
                    .forEach(fieldList::add);
            clazz = clazz.getSuperclass();
        }
        return fieldList.toArray(new Field[0]);
    }

    /**
     * Get all declared fields (include inherited) with given type of class
     * @param clazz the class
     * @return fields array
     */
    public static Field[] getAllDeclaredFieldsWithType(Class<?> clazz, Class<?> fieldType) {
        List<Field> fieldList = new ArrayList<>();
        while (clazz != null) {
            Arrays.stream(clazz.getDeclaredFields())
                    .filter(field -> field.getType() == fieldType)
                    .forEach(fieldList::add);
            clazz = clazz.getSuperclass();
        }
        return fieldList.toArray(new Field[0]);
    }
}
