package com.hzboiler.erp.core.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.hzboiler.erp.core.field.Many2One;
import com.hzboiler.erp.core.field.One2Many;
import com.hzboiler.erp.core.field.annotations.InverseField;
import com.hzboiler.erp.core.validation.CreateValidationGroup;
import com.hzboiler.erp.core.validation.UpdateValidationGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Null;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

/**
 * @author gongshuiwen
 */
@Getter
@Setter
public abstract class BaseTreeModel<T extends BaseTreeModel<?>> extends BaseModel {

    @Schema(description = "父级路径")
    @Null(groups = {CreateValidationGroup.class, UpdateValidationGroup.class})
    private String parentPath;

    @Schema(description = "父级ID")
    private Many2One<T> parentId;

    @Schema(description = "子级列表")
    @TableField(exist = false)
    @InverseField("parentId")
    private One2Many<T> children;

    /**
     * build a flat record list to a hierarchical tree, return roots' list
     *
     * @param records flat record list
     * @return roots' list
     */
    public static <T extends BaseTreeModel<T>> List<T> buildTree(List<T> records) {
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
