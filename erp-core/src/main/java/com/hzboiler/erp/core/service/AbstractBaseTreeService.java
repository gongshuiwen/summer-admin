package com.hzboiler.erp.core.service;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hzboiler.erp.core.field.Many2One;
import com.hzboiler.erp.core.model.BaseTreeModel;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

public abstract class AbstractBaseTreeService<M extends BaseMapper<T>, T extends BaseTreeModel<T>>
        extends AbstractBaseService<M, T>
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
    @Transactional(rollbackFor = Exception.class)
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

    private String getParentPath(T record) {
        if (record == null)
            throw new IllegalArgumentException("record must not be null");

        if (record.getId() == null)
            throw new IllegalArgumentException("record's id must not be null");

        return record.getParentPath() == null || record.getParentPath().isEmpty() ?
                record.getId().toString() :
                record.getParentPath() + "/" + record.getId();
    }
}
