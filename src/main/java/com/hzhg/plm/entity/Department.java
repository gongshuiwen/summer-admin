package com.hzhg.plm.entity;

import com.hzhg.plm.core.entity.TreeBaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@Schema(description = "部门信息")
public class Department extends TreeBaseEntity<Department> {

    private static final long serialVersionUID = 1L;

    @Schema(description = "部门名称")
    @NotBlank(message = "部门名称不能为空")
    @Size(max = 18, message = "部门名称长度不能超过18个字符")
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
