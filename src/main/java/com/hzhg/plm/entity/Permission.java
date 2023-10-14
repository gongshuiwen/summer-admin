package com.hzhg.plm.entity;


import com.hzhg.plm.core.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "权限信息")
public class Permission  extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "权限名称")
    private String name;

    @Schema(description = "显示顺序")
    private Integer orderNum;

    @Schema(description = "状态 1=正常,0=停用")
    private Integer status;
}
