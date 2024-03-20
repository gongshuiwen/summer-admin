package com.hzhg.plm.entity;

import com.hzhg.plm.core.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Getter
@Setter
@Schema(description = "角色信息")
public class Role extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "角色标识")
    @NotBlank(message = "角色名称不能为空")
    @Size(max = 18, message = "角色名称长度不能超过18个字符")
    private String code;

    @Schema(description = "角色名称")
    @NotBlank(message = "角色名称不能为空")
    @Size(max = 18, message = "角色名称长度不能超过18个字符")
    private String name;

    @Schema(description = "显示顺序")
    private Integer orderNum;

    @Schema(description = "状态 1=正常,0=停用")
    private Integer status;

    @Override
    public String getDisplayName() {
        return getName();
    }
}
