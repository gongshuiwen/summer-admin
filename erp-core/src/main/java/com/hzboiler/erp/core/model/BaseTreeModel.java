package com.hzboiler.erp.core.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.hzboiler.erp.core.validation.CreateValidationGroup;
import com.hzboiler.erp.core.validation.UpdateValidationGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Null;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author gongshuiwen
 */
@Getter
@Setter
public abstract class BaseTreeModel<T extends BaseTreeModel<?>> extends BaseModel {

    @Schema(description = "父级ID")
    private Long parentId;

    @Schema(description = "父级路径")
    @Null(groups = {CreateValidationGroup.class, UpdateValidationGroup.class})
    private String parentPath;

    @Schema(description = "子级列表")
    @TableField(exist = false)
    private List<T> children;

    /**
     * build a flat record list to a hierarchical tree, return roots' list
     *
     * @param records flat record list
     * @return roots' list
     */
    public static <T extends BaseTreeModel<T>> List<T> buildTree(List<T> records) {
        List<T> roots = new ArrayList<>();
        Map<Long, T> recordMap = new HashMap<>(records.size());

        // Loop 1: Build record map using id as key
        records.forEach(record -> recordMap.put(record.getId(), record));

        // Loop 2
        records.forEach(record -> {
            if (record.getParentId() == null || record.getParentId() == 0) {
                // Add root record to roots' list
                roots.add(record);
            } else {
                // Add leaf record to parent's children list
                List<T> children = recordMap.get(record.getParentId()).getChildren();
                if (children == null) {
                    children = new ArrayList<>();
                }
                children.add(record);
            }
        });

        return roots;
    }
}
