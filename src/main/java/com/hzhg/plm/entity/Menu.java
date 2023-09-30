package com.hzhg.plm.entity;

import com.hzhg.plm.common.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Schema(description = "菜单信息")
public class Menu extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "菜单ID")
    private Long id;

    @Schema(description = "菜单名称")
    private String name;

    @Schema(description = "父菜单ID")
    private Long parentId;

    @Schema(description = "父菜单名称")
    private String parentName;

    @Schema(description = "显示顺序")
    private Integer orderNum;

    @Schema(description = "菜单状态 0=正常,1=停用")
    private String status;

    @Schema(description = "路由地址")
    private String path;

    @Schema(description = "组件路径")
    private String component;

    @Schema(description = "路由参数")
    private String query;

    @Schema(description = "是否外链")
    private String isFrame;

    @Schema(description = "是否缓存（0缓存 1不缓存）")
    private String isCache;

    @Schema(description = "类型（M目录 C菜单 F按钮）")
    private String menuType;

    @Schema(description = "显示状态（0显示 1隐藏） ")
    private String visible;

    @Schema(description = "权限字符串")
    private String perms;

    @Schema(description = "菜单图标")
    private String icon;

    @Schema(description = "子菜单")
    private List<Menu> children = new ArrayList<>();

    @NotBlank(message = "菜单名称不能为空")
    @Size(max = 50, message = "菜单名称长度不能超过50个字符")
    public String getName() {
        return name;
    }

    @NotNull(message = "显示顺序不能为空")
    public Integer getOrderNum() {
        return orderNum;
    }

    @Size(max = 200, message = "路由地址不能超过200个字符")
    public String getPath() {
        return path;
    }

    @Size(max = 200, message = "组件路径不能超过255个字符")
    public String getComponent() {
        return component;
    }

    @NotBlank(message = "菜单类型不能为空")
    public String getMenuType() {
        return menuType;
    }

    @Size(max = 100, message = "权限标识长度不能超过100个字符")
    public String getPerms() {
        return perms;
    }
}
