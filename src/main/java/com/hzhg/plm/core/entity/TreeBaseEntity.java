package com.hzhg.plm.core.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
public abstract class TreeBaseEntity<T extends TreeBaseEntity<?>> extends BaseEntity {

    @Schema(description = "父级ID")
    private Long parentId;

    @Schema(description = "祖级路径")
    private String parentPath;

    @Schema(description = "子级列表")
    @TableField(exist = false)
    private List<T> children = new ArrayList<>();
}
