package com.hzhg.plm.entity;

import com.hzhg.plm.common.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
@Schema(description = "角色信息")
public class Role extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    @Schema(description = "角色ID")
    private Long id;

    @Schema(description = "角色名称")
    private String roleName;

    @Schema(description = "显示顺序")
    private Integer orderNum;

    @Schema(description = "角色状态 0=正常,1=停用")
    private String status;

    @Schema(description = "角色权限")
    private String roleKey;

    @Schema(description = "数据范围 1=所有数据权限,2=自定义数据权限,3=本部门数据权限,4=本部门及以下数据权限,5=仅本人数据权限")
    private String dataScope;

    @Schema(description = "菜单树选择项是否关联显示 0=父子不互相关联显示,1=父子互相关联显示")
    private boolean menuCheckStrictly;

    @Schema(description = "部门树选择项是否关联显示 0=父子不互相关联显示,1=父子互相关联显示")
    private boolean deptCheckStrictly;

    @Schema(description = "菜单组")
    private Long[] menuIds;

    @Schema(description = "部门组")
    private Long[] deptIds;

    @Schema(description = "角色菜单权限")
    private Set<String> permissions;

    public boolean isAdmin() {
        return isAdmin(this.id);
    }

    public static boolean isAdmin(Long roleId) {
        return roleId != null && 1L == roleId;
    }

    @NotBlank(message = "角色名称不能为空")
    @Size(max = 30, message = "角色名称长度不能超过30个字符")
    public String getRoleName() {
        return roleName;
    }

    @NotNull(message = "显示顺序不能为空")
    public Integer getOrderNum() {
        return orderNum;
    }

    @NotBlank(message = "权限字符不能为空")
    @Size(max = 100, message = "权限字符长度不能超过100个字符")
    public String getRoleKey() {
        return roleKey;
    }
}
