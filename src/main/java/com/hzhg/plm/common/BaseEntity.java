package com.hzhg.plm.common;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;


@Getter
@Setter
public class BaseEntity implements Serializable {

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
