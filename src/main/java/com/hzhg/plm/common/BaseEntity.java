package com.hzhg.plm.common;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public class BaseEntity {

    @Schema(name = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @Schema(name = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @Schema(name = "创建用户")
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    @Schema(name = "更新用户")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;
}
