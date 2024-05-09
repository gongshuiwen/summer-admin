package com.hzboiler.base.entity;

import com.hzboiler.core.entity.TreeBaseEntity;
import com.hzboiler.core.validation.CreateValidationGroup;
import com.hzboiler.core.validation.NullOrNotBlank;
import com.hzboiler.core.validation.UpdateValidationGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.Size;

import java.io.Serial;

@Getter
@Setter
@Schema(description = "部门信息")
public class Department extends TreeBaseEntity<Department> {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "状态 1=正常,0=停用")
    private Integer status;

    @Schema(description = "显示顺序")
    private Integer orderNum;

    @Schema(description = "部门名称")
    @NotNull(groups = {CreateValidationGroup.class})
    @NullOrNotBlank(groups = {CreateValidationGroup.class, UpdateValidationGroup.class})
    @Size(min = 2, max = 18, groups = {CreateValidationGroup.class, UpdateValidationGroup.class})
    private String name;
}
