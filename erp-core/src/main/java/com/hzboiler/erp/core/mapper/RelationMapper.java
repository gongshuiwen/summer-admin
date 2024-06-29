package com.hzboiler.erp.core.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author gongshuiwen
 */
public interface RelationMapper {

    // Cache for RelationMapper's MapperRelation annotation
    Map<Class<?>, MapperRelation> mapperRelationCache = new ConcurrentHashMap<>();

    @Select("SELECT ${targetField} FROM ${table} WHERE ${sourceField} = #{sourceId}")
    List<Long> _getTargetIdsById(String table, String sourceField, String targetField, Long sourceId);

    default List<Long> getTargetIds(Class<?> sourceClass, Long sourceId) {
        Objects.requireNonNull(sourceClass, "sourceClass must not be null");
        Objects.requireNonNull(sourceId, "sourceId must not be null");

        // Get the mapper interface
        Class<?> mapperInterface = this.getClass().getInterfaces()[0];

        // Get the MapperRelation
        MapperRelation mapperRelation = mapperRelationCache.get(mapperInterface);

        // Get the source field and target field info
        FieldInfo fieldInfo = getFieldInfo(mapperRelation, sourceClass);

        return _getTargetIdsById(mapperRelation.table(), fieldInfo.sourceField(), fieldInfo.targetField(), sourceId);
    }

    @Select("SELECT ${targetField} FROM ${table} WHERE ${sourceField} IN (${sourceIds})")
    List<Long> _getTargetIdsByIds(String table, String sourceField, String targetField, String sourceIds);

    default List<Long> getTargetIds(Class<?> sourceClass, List<Long> sourceIds) {
        Objects.requireNonNull(sourceClass, "sourceClass must not be null");
        if (sourceIds == null || sourceIds.isEmpty())
            throw new IllegalArgumentException("sourceIds must not be null or empty");

        // Get the mapper interface
        Class<?> mapperInterface = this.getClass().getInterfaces()[0];

        // Get the MapperRelation
        MapperRelation mapperRelation = mapperRelationCache.get(mapperInterface);

        // Get the source field and target field info
        FieldInfo fieldInfo = getFieldInfo(mapperRelation, sourceClass);

        String sourceIdsString = sourceIds.stream().distinct().map(String::valueOf).collect(Collectors.joining(","));
        return _getTargetIdsByIds(mapperRelation.table(), fieldInfo.sourceField(), fieldInfo.targetField(), sourceIdsString);
    }

    @Insert("INSERT INTO ${table} (${sourceField}, ${targetField}) VALUES ${values}")
    void _add(String table, String sourceField, String targetField, String values);

    @SuppressWarnings("Duplicates")
    default void add(Class<?> sourceClass, Long sourceId, List<Long> targetIds) {
        Objects.requireNonNull(sourceClass, "sourceClass must not be null");
        Objects.requireNonNull(sourceId, "sourceId must not be null");
        if (targetIds == null || targetIds.isEmpty())
            throw new IllegalArgumentException("targetIds must not be null or empty");

        // Get the mapper interface
        Class<?> mapperInterface = this.getClass().getInterfaces()[0];

        // Get the MapperRelation
        MapperRelation mapperRelation = mapperRelationCache.get(mapperInterface);

        // Get the source field and target field info
        FieldInfo fieldInfo = getFieldInfo(mapperRelation, sourceClass);

        // Get existing targetIds
        List<Long> existingTargetIds = _getTargetIdsById(mapperRelation.table(), fieldInfo.sourceField(), fieldInfo.targetField(), sourceId);

        // Filter targetIds
        targetIds = targetIds.stream().filter(roleId -> !existingTargetIds.contains(roleId)).toList();

        // Add new rows if targetIds is not empty
        if (!targetIds.isEmpty())
            _add(mapperRelation.table(), fieldInfo.sourceField(), fieldInfo.targetField(), buildValues(sourceId, targetIds));
    }

    @SuppressWarnings("Duplicates")
    default void unsafeAdd(Class<?> sourceClass, Long sourceId, List<Long> targetIds) {
        Objects.requireNonNull(sourceClass, "sourceClass must not be null");
        Objects.requireNonNull(sourceId, "sourceId must not be null");
        if (targetIds == null || targetIds.isEmpty())
            throw new IllegalArgumentException("targetIds must not be null or empty");

        // Get the mapper interface
        Class<?> mapperInterface = this.getClass().getInterfaces()[0];

        // Get the MapperRelation
        MapperRelation mapperRelation = mapperRelationCache.get(mapperInterface);

        // Get the source field and target field info
        FieldInfo fieldInfo = getFieldInfo(mapperRelation, sourceClass);

        _add(mapperRelation.table(), fieldInfo.sourceField(), fieldInfo.targetField(), buildValues(sourceId, targetIds));
    }

    @Delete("DELETE FROM ${table} WHERE ${sourceField} = #{sourceId} and ${targetField} IN (${targetIdsString})")
    void _remove(String table, String sourceField, String targetField, Long sourceId, String targetIdsString);

    @SuppressWarnings("Duplicates")
    default void remove(Class<?> sourceClass, Long sourceId, List<Long> targetIds) {
        Objects.requireNonNull(sourceClass, "sourceClass must not be null");
        Objects.requireNonNull(sourceId, "sourceId must not be null");
        if (targetIds == null || targetIds.isEmpty())
            throw new IllegalArgumentException("targetIds must not be null or empty");

        // Get the mapper interface
        Class<?> mapperInterface = this.getClass().getInterfaces()[0];

        // Get the MapperRelation
        MapperRelation mapperRelation = mapperRelationCache.get(mapperInterface);

        // Get the source field and target field info
        FieldInfo fieldInfo = getFieldInfo(mapperRelation, sourceClass);

        String targetIdsString = targetIds.stream().map(String::valueOf).collect(Collectors.joining(","));
        _remove(mapperRelation.table(), fieldInfo.sourceField(), fieldInfo.targetField(), sourceId, targetIdsString);
    }

    @Delete("DELETE FROM ${table} WHERE ${sourceField} = #{sourceId}")
    void _removeAll(String table, String sourceField, Long sourceId);

    default void removeAll(Class<?> sourceClass, Long sourceId) {
        Objects.requireNonNull(sourceClass, "sourceClass must not be null");
        Objects.requireNonNull(sourceId, "sourceId must not be null");

        // Get the mapper interface
        Class<?> mapperInterface = this.getClass().getInterfaces()[0];

        // Get the MapperRelation
        MapperRelation mapperRelation = mapperRelationCache.get(mapperInterface);

        // Get the source field and target field info
        FieldInfo fieldInfo = getFieldInfo(mapperRelation, sourceClass);

        _removeAll(mapperRelation.table(), fieldInfo.sourceField(), sourceId);
    }

    @SuppressWarnings("Duplicates")
    default void replace(Class<?> sourceClass, Long sourceId, List<Long> targetIds) {
        Objects.requireNonNull(sourceClass, "sourceClass must not be null");
        Objects.requireNonNull(sourceId, "sourceId must not be null");
        Objects.requireNonNull(targetIds, "targetIds must not be null");

        // Get the mapper interface
        Class<?> mapperInterface = this.getClass().getInterfaces()[0];

        // Get the MapperRelation
        MapperRelation mapperRelation = mapperRelationCache.get(mapperInterface);

        // Get the source field and target field info
        FieldInfo fieldInfo = getFieldInfo(mapperRelation, sourceClass);

        // Remove all
        _removeAll(mapperRelation.table(), fieldInfo.sourceField(), sourceId);

        // Add new rows if targetIds is not empty
        if (!targetIds.isEmpty())
            _add(mapperRelation.table(), fieldInfo.sourceField(), fieldInfo.targetField(), buildValues(sourceId, targetIds));
    }

    private String buildValues(Long sourceId, List<Long> targetIds) {
        return targetIds.stream().distinct().map((t) -> "(" + sourceId + "," + t + ")").collect(Collectors.joining(","));
    }

    private FieldInfo getFieldInfo(MapperRelation mapperRelation, Class<?> sourceClass) {
        if (sourceClass == mapperRelation.class1()) {
            return new FieldInfo(mapperRelation.field1(), mapperRelation.field2());
        } else if (sourceClass == mapperRelation.class2()) {
            return new FieldInfo(mapperRelation.field2(), mapperRelation.field1());
        } else {
            throw new IllegalArgumentException("The sourceClass '" + sourceClass + "' is invalid!");
        }
    }

    record FieldInfo(String sourceField, String targetField) {
    }
}
