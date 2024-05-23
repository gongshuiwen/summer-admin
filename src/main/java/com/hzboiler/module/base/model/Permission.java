package com.hzboiler.module.base.model;


import com.hzboiler.core.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.io.Serial;

@Getter
@Setter
@Schema(description = "权限信息")
public class Permission  extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "权限标识")
    @NotBlank(message = "权限标识不能为空")
    @Size(max = 18, message = "权限标识长度不能超过18个字符")
    private String code;

    @Schema(description = "权限名称")
    @NotBlank(message = "权限名称不能为空")
    @Size(max = 18, message = "权限名称长度不能超过18个字符")
    private String name;

    @Schema(description = "显示顺序")
    private Integer orderNum;

    @Schema(description = "状态 1=正常,0=停用")
    private Integer status;
}
