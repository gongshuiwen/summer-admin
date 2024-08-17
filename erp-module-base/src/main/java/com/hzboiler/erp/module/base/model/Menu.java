package com.hzboiler.erp.module.base.model;

import com.hzboiler.erp.common.validation.CreateValidationGroup;
import com.hzboiler.erp.common.validation.NullOrNotBlank;
import com.hzboiler.erp.core.model.BaseTreeModel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

@Getter
@Setter
@Schema(description = "菜单信息")
public class Menu extends BaseTreeModel<Menu> {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "状态 1=正常,0=停用")
    private Integer status;

    @Schema(description = "显示顺序")
    private Integer orderNum;

    @Schema(description = "菜单名称")
    @NotNull(groups = {CreateValidationGroup.class})
    @NullOrNotBlank
    @Size(min = 2, max = 18)
    private String name;

    @Schema(description = "菜单标题")
    @NotNull(groups = {CreateValidationGroup.class})
    @NullOrNotBlank
    @Size(min = 2, max = 18)
    private String title;

    @Schema(description = "路由地址")
    @NotNull(groups = {CreateValidationGroup.class})
    @NullOrNotBlank
    @Size(max = 18)
    private String path;

    @Schema(description = "菜单图标")
    @Size(max = 18)
    private String icon;

    @Schema(description = "组件路径")
    @NullOrNotBlank
    @Size(max = 50)
    private String component;

    @Schema(description = "重定向")
    @NullOrNotBlank
    @Size(max = 50)
    private String redirect;

    @Override
    public String getDisplayName() {
        return getTitle();
    }
}
