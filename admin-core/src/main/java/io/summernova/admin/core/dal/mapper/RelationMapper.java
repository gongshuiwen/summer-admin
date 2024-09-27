package io.summernova.admin.core.dal.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author gongshuiwen
 */
public interface RelationMapper {

    default RelationMapperInfo getRelationMapperInfo() {
        // Get the mapper interface from proxy class
        Class<?> mapperInterface = this.getClass().getInterfaces()[0];

        return RelationMapperRegistry.getRelationMapperInfo(mapperInterface);
    }

    @Select("SELECT ${targetField} FROM ${table} WHERE ${sourceField} = #{sourceId}")
    List<Long> _getTargetIdsById(String table, String sourceField, String targetField, Long sourceId);

    default List<Long> getTargetIds(Long sourceId) {
        Objects.requireNonNull(sourceId, "sourceId must not be null");

        // Get the RelationMapperInfo
        RelationMapperInfo relationMapperInfo = getRelationMapperInfo();

        return _getTargetIdsById(relationMapperInfo.joinTable(), relationMapperInfo.sourceField(), relationMapperInfo.targetField(), sourceId);
    }

    @Select("SELECT ${targetField} FROM ${table} WHERE ${sourceField} IN (${sourceIds})")
    List<Long> _getTargetIdsByIds(String table, String sourceField, String targetField, String sourceIds);

    default List<Long> getTargetIds(List<Long> sourceIds) {
        if (sourceIds == null || sourceIds.isEmpty())
            throw new IllegalArgumentException("sourceIds must not be null or empty");

        // Get the RelationMapperInfo
        RelationMapperInfo relationMapperInfo = getRelationMapperInfo();

        String sourceIdsString = sourceIds.stream().distinct().map(String::valueOf).collect(Collectors.joining(","));
        return _getTargetIdsByIds(relationMapperInfo.joinTable(), relationMapperInfo.sourceField(), relationMapperInfo.targetField(), sourceIdsString);
    }

    @Insert("INSERT INTO ${table} (${sourceField}, ${targetField}) VALUES ${values}")
    void _add(String table, String sourceField, String targetField, String values);

    @SuppressWarnings("Duplicates")
    default void add(Long sourceId, List<Long> targetIds) {
        Objects.requireNonNull(sourceId, "sourceId must not be null");
        if (targetIds == null || targetIds.isEmpty())
            throw new IllegalArgumentException("targetIds must not be null or empty");

        // Get the RelationMapperInfo
        RelationMapperInfo relationMapperInfo = getRelationMapperInfo();

        // Get existing targetIds
        List<Long> existingTargetIds = _getTargetIdsById(relationMapperInfo.joinTable(), relationMapperInfo.sourceField(), relationMapperInfo.targetField(), sourceId);

        // Filter targetIds
        targetIds = targetIds.stream().filter(roleId -> !existingTargetIds.contains(roleId)).toList();

        // Add new rows if targetIds is not empty
        if (!targetIds.isEmpty())
            _add(relationMapperInfo.joinTable(), relationMapperInfo.sourceField(), relationMapperInfo.targetField(), buildValues(sourceId, targetIds));
    }

    @SuppressWarnings("Duplicates")
    default void unsafeAdd(Long sourceId, List<Long> targetIds) {
        Objects.requireNonNull(sourceId, "sourceId must not be null");
        if (targetIds == null || targetIds.isEmpty())
            throw new IllegalArgumentException("targetIds must not be null or empty");

        // Get the RelationMapperInfo
        RelationMapperInfo relationMapperInfo = getRelationMapperInfo();

        _add(relationMapperInfo.joinTable(), relationMapperInfo.sourceField(), relationMapperInfo.targetField(), buildValues(sourceId, targetIds));
    }

    @Delete("DELETE FROM ${table} WHERE ${sourceField} = #{sourceId} and ${targetField} IN (${targetIdsString})")
    void _remove(String table, String sourceField, String targetField, Long sourceId, String targetIdsString);

    @SuppressWarnings("Duplicates")
    default void remove(Long sourceId, List<Long> targetIds) {
        Objects.requireNonNull(sourceId, "sourceId must not be null");
        if (targetIds == null || targetIds.isEmpty())
            throw new IllegalArgumentException("targetIds must not be null or empty");

        // Get the RelationMapperInfo
        RelationMapperInfo relationMapperInfo = getRelationMapperInfo();

        String targetIdsString = targetIds.stream().map(String::valueOf).collect(Collectors.joining(","));
        _remove(relationMapperInfo.joinTable(), relationMapperInfo.sourceField(), relationMapperInfo.targetField(), sourceId, targetIdsString);
    }

    @Delete("DELETE FROM ${table} WHERE ${sourceField} = #{sourceId}")
    void _removeAll(String table, String sourceField, Long sourceId);

    default void removeAll(Long sourceId) {
        Objects.requireNonNull(sourceId, "sourceId must not be null");

        // Get the RelationMapperInfo
        RelationMapperInfo relationMapperInfo = getRelationMapperInfo();

        _removeAll(relationMapperInfo.joinTable(), relationMapperInfo.sourceField(), sourceId);
    }

    @SuppressWarnings("Duplicates")
    default void replace(Long sourceId, List<Long> targetIds) {
        Objects.requireNonNull(sourceId, "sourceId must not be null");
        Objects.requireNonNull(targetIds, "targetIds must not be null");

        // Get the RelationMapperInfo
        RelationMapperInfo relationMapperInfo = getRelationMapperInfo();

        // Remove all
        _removeAll(relationMapperInfo.joinTable(), relationMapperInfo.sourceField(), sourceId);

        // Add new rows if targetIds is not empty
        if (!targetIds.isEmpty())
            _add(relationMapperInfo.joinTable(), relationMapperInfo.sourceField(), relationMapperInfo.targetField(), buildValues(sourceId, targetIds));
    }

    private String buildValues(Long sourceId, List<Long> targetIds) {
        return targetIds.stream().distinct().map((t) -> "(" + sourceId + "," + t + ")").collect(Collectors.joining(","));
    }
}
