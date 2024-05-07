package com.hzboiler.core.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public interface RelationMapper {

    Map<Class<?>, String> mapperTables = new HashMap<>();
    Map<Class<?>, String> mapperField1 = new HashMap<>();
    Map<Class<?>, String> mapperField2 = new HashMap<>();
    Map<Class<?>, Class<?>> mapperClass1 = new HashMap<>();
    Map<Class<?>, Class<?>> mapperClass2 = new HashMap<>();

    @Select("SELECT ${targetField} FROM ${table} WHERE ${sourceField} = #{sourceId}")
    List<Long> _getTargetIdsById(String table, String sourceField, String targetField, Long sourceId);

    default List<Long> getTargetIds(Class<?> sourceClass, Long sourceId) {
        if (sourceClass == null) {
            throw new RuntimeException("sourceClass must not be null!");
        }

        if (sourceId == null) {
            throw new RuntimeException("sourceId must not be null!");
        }

        // Get mapper interface
        Class<?> mapperInterface = this.getClass().getInterfaces()[0];

        // Get table name
        String table = mapperTables.get(mapperInterface);

        // Get the source field and target field
        Map<String, String> fieldInfo = getFieldInfo(mapperInterface, sourceClass);
        String sourceField = fieldInfo.get("sourceField");
        String targetField = fieldInfo.get("targetField");

        return _getTargetIdsById(table, sourceField, targetField, sourceId);
    }

    @Select("SELECT ${targetField} FROM ${table} WHERE ${sourceField} IN (${sourceIds})")
    List<Long> _getTargetIdsByIds(String table, String sourceField, String targetField, String sourceIds);

    default List<Long> getTargetIds(Class<?> sourceClass, List<Long> sourceIds) {
        if (sourceClass == null) {
            throw new RuntimeException("sourceClass must not be null!");
        }

        if (sourceIds == null || sourceIds.isEmpty()) {
            throw new RuntimeException("sourceIds must not be null or empty!");
        }

        // Get mapper interface
        Class<?> mapperInterface = this.getClass().getInterfaces()[0];

        // Get table name
        String table = mapperTables.get(mapperInterface);

        // Get the source field and target field
        Map<String, String> fieldInfo = getFieldInfo(mapperInterface, sourceClass);
        String sourceField = fieldInfo.get("sourceField");
        String targetField = fieldInfo.get("targetField");

        String sourceIdsString = sourceIds.stream().map(String::valueOf).collect(Collectors.joining(","));
        return _getTargetIdsByIds(table, sourceField, targetField, sourceIdsString);
    }

    @Insert("INSERT INTO ${table} (${sourceField}, ${targetField}) VALUES ${values}")
    void _add(String table, String sourceField, String targetField, String values);

    default void add(Class<?> sourceClass, Long sourceId, List<Long> targetIds) {
        if (sourceClass == null) {
            throw new RuntimeException("sourceClass must not be null!");
        }

        if (sourceId == null) {
            throw new RuntimeException("sourceId must not be null!");
        }

        if (targetIds == null || targetIds.isEmpty()) {
            throw new RuntimeException("targetIds must not be null or empty!");
        }

        // Get mapper interface
        Class<?> mapperInterface = this.getClass().getInterfaces()[0];

        // Get table name
        String table = mapperTables.get(mapperInterface);

        // Get the source field and target field
        Map<String, String> fieldInfo = getFieldInfo(mapperInterface, sourceClass);
        String sourceField = fieldInfo.get("sourceField");
        String targetField = fieldInfo.get("targetField");

        StringBuilder sb = new StringBuilder();
        targetIds.forEach(targetId -> sb.append("(").append(sourceId).append(", ").append(targetId).append("),"));
        sb.deleteCharAt(sb.length() - 1);
        _add(table, sourceField, targetField, sb.toString());
    }

    @Delete("DELETE FROM ${table} WHERE ${sourceField} = #{sourceId} and ${targetField} IN (${targetIdsString})")
    void _remove(String table, String sourceField, String targetField, Long sourceId, String targetIdsString);

    default void remove(Class<?> sourceClass, Long sourceId, List<Long> targetIds) {
        if (sourceClass == null) {
            throw new RuntimeException("sourceClass must not be null!");
        }

        if (sourceId == null) {
            throw new RuntimeException("sourceId must not be null!");
        }

        if (targetIds == null || targetIds.isEmpty()) {
            throw new RuntimeException("targetIds must not be null or empty!");
        }

        // Get mapper interface
        Class<?> mapperInterface = this.getClass().getInterfaces()[0];

        // Get table name
        String table = mapperTables.get(mapperInterface);

        // Get the source field and target field
        Map<String, String> fieldInfo = getFieldInfo(mapperInterface, sourceClass);
        String sourceField = fieldInfo.get("sourceField");
        String targetField = fieldInfo.get("targetField");

        String targetIdsString = targetIds.stream().map(String::valueOf).collect(Collectors.joining(","));
        _remove(table, sourceField, targetField, sourceId, targetIdsString);
    }

    @Delete("DELETE FROM ${table} WHERE ${sourceField} = #{sourceId}")
    void _removeAll(String table, String sourceField, Long sourceId);

    default void removeAll(Class<?> sourceClass, Long sourceId) {
        if (sourceClass == null) {
            throw new RuntimeException("sourceClass must not be null!");
        }

        if (sourceId == null) {
            throw new RuntimeException("sourceId must not be null!");
        }

        // Get mapper interface
        Class<?> mapperInterface = this.getClass().getInterfaces()[0];

        // Get table name
        String table = mapperTables.get(mapperInterface);

        // Get the source field and target field
        Map<String, String> fieldInfo = getFieldInfo(mapperInterface, sourceClass);
        String sourceField = fieldInfo.get("sourceField");

        _removeAll(table, sourceField, sourceId);
    }

    default void replace(Class<?> sourceClass, Long sourceId, List<Long> targetIds) {
        // TODO: improve this implement
        removeAll(sourceClass, sourceId);
        add(sourceClass, sourceId, targetIds);
    }

    private Map<String, String> getFieldInfo(Class<?> mapperInterface, Class<?> sourceClass) {
        Map<String, String> map = new HashMap<>();
        if (sourceClass == mapperClass1.get(mapperInterface)) {
            map.put("sourceField", mapperField1.get(mapperInterface));
            map.put("targetField", mapperField2.get(mapperInterface));
        } else if (sourceClass == mapperClass2.get(mapperInterface)) {
            map.put("sourceField", mapperField2.get(mapperInterface));
            map.put("targetField", mapperField1.get(mapperInterface));
        } else {
            throw new RuntimeException("The sourceClass '" + sourceClass + "' is invalid for RelationMapper '" + mapperInterface + "'!");
        }
        return map;
    }
}
