package com.hzhg.plm.entity;

import com.hzhg.plm.common.TreeBaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@Schema(description = "部门信息")
public class Department extends TreeBaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "显示顺序")
    private Integer orderNum;

    @Schema(description = "状态 0=正常,1=停用")
    private String status;

    @Schema(description = "部门名称")
    private String name;

    @NotBlank(message = "部门名称不能为空")
    @Size(max = 30, message = "部门名称长度不能超过30个字符")
    public String getName() {
        return name;
    }

    @NotNull(message = "显示顺序不能为空")
    public Integer getOrderNum() {
        return orderNum;
    }
}
