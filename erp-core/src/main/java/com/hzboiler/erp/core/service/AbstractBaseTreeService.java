package com.hzboiler.erp.core.service;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hzboiler.erp.core.model.BaseTreeModel;

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

        // Extract the ids from the parent path, convert them to Long, and retrieve the entities
        List<Long> ids = Arrays.stream(record.getParentPath().split("/"))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        return selectByIds(ids);
    }

    @Override
    public List<T> getDescendants(T record) {
        Objects.requireNonNull(record, "record must not be null");

        // Get parent path prefix of the descendants
        String parentPathPrefix = record.getParentPath() == null || record.getParentPath().isEmpty() ?
                record.getId().toString() :
                record.getParentPath() + "/" + record.getId();
        return lambdaQuery().likeRight(T::getParentPath, parentPathPrefix).list();
    }
}
