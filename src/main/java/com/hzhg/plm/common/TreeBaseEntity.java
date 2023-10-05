package com.hzhg.plm.common;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.hzhg.plm.entity.Menu;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
public class TreeBaseEntity extends BaseEntity {

    @Schema(description = "父级ID")
    private Long parentId;

    @Schema(description = "祖级路径")
    private String parentPath;

    @Schema(description = "子级列表")
    @TableField(exist = false)
    private List<TreeBaseEntity> children = new ArrayList<>();
}
