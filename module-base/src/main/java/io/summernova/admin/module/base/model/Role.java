package io.summernova.admin.module.base.model;

import com.baomidou.mybatisplus.annotation.TableField;
import io.summernova.admin.common.validation.CreateValidationGroup;
import io.summernova.admin.common.validation.NullOrNotBlank;
import io.summernova.admin.core.domain.field.Many2Many;
import io.summernova.admin.core.domain.field.annotations.Many2ManyField;
import io.summernova.admin.core.domain.model.BaseModel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

/**
 * @author gongshuiwen
 */
@Getter
@Setter
@Schema(description = "角色信息")
public class Role extends BaseModel {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "角色标识")
    @NotNull(groups = {CreateValidationGroup.class})
    @NullOrNotBlank
    @Size(min = 2, max = 18)
    private String code;

    @Schema(description = "角色名称")
    @NotNull(groups = {CreateValidationGroup.class})
    @NullOrNotBlank
    @Size(min = 2, max = 18)
    private String name;

    @Schema(description = "显示顺序")
    private Integer orderNum;

    @Schema(description = "状态 1=正常,0=停用")
    private Integer status;

    @TableField(exist = false)
    @Many2ManyField(sourceField = "role_id", targetField = "perm_id", joinTable = "role_permission")
    private Many2Many<Permission> permissions;
}
