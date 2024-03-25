package com.hzhg.plm.core.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.hzhg.plm.core.validation.CreateValidationGroup;
import com.hzhg.plm.core.validation.UpdateValidationGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Null;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Getter
@Setter
public abstract class TreeBaseEntity<T extends TreeBaseEntity<?>> extends BaseEntity {

    @Schema(description = "父级ID")
    private Long parentId;

    @Schema(description = "祖级路径")
    @Null(groups = {CreateValidationGroup.class, UpdateValidationGroup.class})
    private String parentPath;

    @Schema(description = "子级列表")
    @TableField(exist = false)
    private List<T> children = new ArrayList<>();

    /**
     * build a flat entity list to a hierarchical tree, return roots' list
     * @param entities flat entity list
     * @return roots' list
     */
    public static <T extends TreeBaseEntity<T>> List<T> buildTree(List<T> entities) {
        Map<Long, T> entityMap = new HashMap<>(entities.size());
        List<T> rootEntities = new ArrayList<>();

        // Loop 1: Build entity map using id as key
        entities.forEach(entity -> entityMap.put(entity.getId(), entity));

        // Loop 2
        entities.forEach(entity -> {
            if ( entity.getParentId() == 0 )
                // Add every not-root entity to parent's children list
                rootEntities.add(entity);
            else
                // Add every root entity to roots' list
                entityMap.get(entity.getParentId()).getChildren().add(entity);
        });

        return rootEntities;
    }
}
