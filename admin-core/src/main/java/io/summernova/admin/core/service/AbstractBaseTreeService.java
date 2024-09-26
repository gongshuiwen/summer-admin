package io.summernova.admin.core.service;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import io.summernova.admin.core.domain.field.Many2One;
import io.summernova.admin.core.domain.field.One2Many;
import io.summernova.admin.core.domain.model.BaseTreeModel;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author gongshuiwen
 */
@Slf4j
public abstract class AbstractBaseTreeService<T extends BaseTreeModel<T>>
        extends AbstractBaseService<T>
        implements BaseTreeService<T> {

    @Override
    public List<T> getChildren(T record) {
        Objects.requireNonNull(record, "record must not be null");
        return lambdaQuery().eq(T::getParentId, record.getId()).list();
    }

    @Override
    public List<T> getAncestors(T record) {
        Objects.requireNonNull(record, "record must not be null");

        // If the record has no parent path, return an empty list
        if (record.getParentPath() == null || record.getParentPath().isEmpty())
            return Collections.emptyList();

        // Extract the ids from the parent path, convert them to Long, and retrieve the records
        List<Long> ids = Arrays.stream(record.getParentPath().split("/"))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        return selectByIds(ids);
    }

    @Override
    public List<T> getDescendants(T record) {
        return lambdaQuery().likeRight(T::getParentPath, getParentPath(record)).list();
    }

    @Override
    public boolean createBatch(List<T> records) {
        if (records == null || records.isEmpty())
            return false;

        // prepare parents map
        List<Long> parentIds = records.stream().map(T::getParentId).map(Many2One::getId).toList();
        List<T> parents = getBaseMapper().selectBatchIds(parentIds);
        Map<Long, T> parentsMap = parents.stream().collect(Collectors.toMap(T::getId, t -> t));

        // set parent path
        records.forEach(record -> {
            Many2One<T> parentIdField = record.getParentId();
            if (parentIdField == null) {
                record.setParentPath("");
                return;
            }

            Long parentId = parentIdField.getId();
            if (parentId == null || parentId == 0) {
                record.setParentPath("");
            } else {
                T parent = parentsMap.get(record.getParentId().getId());
                record.setParentPath(getParentPath(parent));
            }
        });
        return super.createBatch(records);
    }

    @Override
    public boolean updateByIds(List<Long> ids, T updateValues) {
        Many2One<T> parentIdField = updateValues.getParentId();
        if (parentIdField != null) {
            // get old records before update
            List<T> oldRecords = selectByIds(ids);

            // update parent path of themselves
            if (parentIdField.getId() == 0) {
                updateValues.setParentPath("");
            } else {
                String parentPath = getParentPath(selectById(parentIdField.getId()));

                // check circular reference
                ids.forEach(id -> {
                    if (Arrays.stream(parentPath.split("/")).anyMatch(id1 -> id1.equals(String.valueOf(id)))) {
                        throw new IllegalArgumentException("circular reference");
                    }
                });

                updateValues.setParentPath(parentPath);
            }

            boolean res = super.updateByIds(ids, updateValues);

            // update parent path of their descendants
            oldRecords.forEach(oldRecord -> {
                List<T> descendants = getDescendants(oldRecord);
                descendants.forEach(descendant -> {
                    String oldParentPath = descendant.getParentPath();
                    String newParentPath = oldParentPath.replace(oldRecord.getParentPath(), updateValues.getParentPath());

                    // remove the first '/'
                    if (updateValues.getParentPath().isEmpty())
                        newParentPath = newParentPath.substring(1);

                    if (log.isDebugEnabled())
                        log.debug("update descendant {}'s parent path from {} to {}", descendant.getId(), oldParentPath, newParentPath);

                    LambdaUpdateWrapper<T> updateWrapper = new LambdaUpdateWrapper<>(getModelClass());
                    updateWrapper.set(T::getParentPath, newParentPath);
                    updateWrapper.eq(T::getId, descendant.getId());
                    getBaseMapper().update(updateWrapper);
                });
            });
            return res;
        }

        return super.updateByIds(ids, updateValues);
    }

    private String getParentPath(T record) {
        if (record == null)
            throw new IllegalArgumentException("record must not be null");

        if (record.getId() == null)
            throw new IllegalArgumentException("record's id must not be null");

        return record.getParentPath() == null || record.getParentPath().isEmpty() ?
                record.getId().toString() :
                record.getParentPath() + "/" + record.getId();
    }

    @Override
    public List<T> buildTree(List<T> records) {
        List<T> roots = new ArrayList<>();
        Map<Long, T> recordsMap = new HashMap<>(records.size());
        Map<Long, List<T>> childrenMap = new HashMap<>();

        // Loop 1: build records map using id as key
        records.forEach(record -> recordsMap.put(record.getId(), record));

        // Loop 2: process hierarchy
        records.forEach(record -> {
            Many2One<T> parentIdField = record.getParentId();
            Objects.requireNonNull(parentIdField, "The parentId of record must not be null!");

            Long parentId = parentIdField.getId();
            if (parentId == null || parentId == 0) {
                // Add root record to roots' list
                roots.add(record);
            } else {
                // Add leaf record to parent's children list
                T parent = recordsMap.get(parentId);
                if (parent == null) {
                    throw new IllegalArgumentException("Parent record with id '" + parentId + "' doesn't exist!");
                }

                List<T> children = childrenMap.computeIfAbsent(parentId, k -> new ArrayList<>());
                children.add(record);
            }
        });

        // Loop 3: fill children field
        records.forEach(record -> {
            List<T> children = childrenMap.get(record.getId());
            if (children != null) {
                record.setChildren(One2Many.ofRecords(children));
            }
        });

        return roots;
    }
}
