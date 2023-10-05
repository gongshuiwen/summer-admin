package com.hzhg.plm.utils;

import com.hzhg.plm.entity.base.TreeBaseEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TreeBaseEntityUtils {

    /**
     * build a flat entity list to a hierarchical tree, return roots' list
     * @param entities flat entity list
     * @return roots' list
     */
    public static <T extends TreeBaseEntity> List<T> buildTree(List<T> entities) {
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
