package io.summernova.admin.core.dal.mapper;

/**
 * @author gongshuiwen
 */
public record RelationMapperInfo(
        Class<?> sourceClass,
        Class<?> targetClass,
        String sourceField,
        String targetField,
        String joinTable) {
}
