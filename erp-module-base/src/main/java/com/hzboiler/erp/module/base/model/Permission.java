package com.hzboiler.erp.module.base.model;

import com.hzboiler.erp.core.model.BaseModel;
import com.hzboiler.erp.core.validation.CreateValidationGroup;
import com.hzboiler.erp.core.validation.NullOrNotBlank;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

@Getter
@Setter
@Schema(description = "权限信息")
public class Permission extends BaseModel {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "权限标识")
    @NotNull(groups = {CreateValidationGroup.class})
    @NullOrNotBlank
    @Size(min = 2, max = 18)
    private String code;

    @Schema(description = "权限名称")
    @NotNull(groups = {CreateValidationGroup.class})
    @NullOrNotBlank
    @Size(min = 2, max = 18)
    private String name;

    @Schema(description = "显示顺序")
    private Integer orderNum;

    @Schema(description = "状态 1=正常,0=停用")
    private Integer status;
}
