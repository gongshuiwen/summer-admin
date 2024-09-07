package io.summernova.admin.core.service;

import io.summernova.admin.core.model.BaseTreeModel;

import java.util.List;

/**
 * @author gongshuiwen
 */
public interface BaseTreeService<T extends BaseTreeModel<T>> extends BaseService<T> {

    /**
     * Retrieves the children of the given record.
     *
     * @param record the record to retrieve children for
     * @return children list of the {@code record}
     */
    List<T> getChildren(T record);

    /**
     * Retrieves the ancestors of the given record.
     *
     * @param record the record to retrieve ancestors for
     * @return ancestors list of the {@code record}
     */
    List<T> getAncestors(T record);

    /**
     * Retrieves the descendants of the given record.
     *
     * @param record the record to retrieve descendants for
     * @return descendants list of the {@code record}
     */
    List<T> getDescendants(T record);

    /**
     * Build a flat record list to a hierarchical tree, return roots' list
     *
     * @param records flat record list
     * @return roots' list
     */
    List<T> buildTree(List<T> records);
}
