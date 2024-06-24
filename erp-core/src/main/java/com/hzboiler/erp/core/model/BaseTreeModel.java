package com.hzboiler.erp.core.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.hzboiler.erp.core.field.Many2One;
import com.hzboiler.erp.core.field.One2Many;
import com.hzboiler.erp.core.field.annotations.InverseField;
import com.hzboiler.erp.core.field.annotations.OnDelete;
import com.hzboiler.erp.core.validation.CreateValidationGroup;
import com.hzboiler.erp.core.validation.UpdateValidationGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Null;
import lombok.Getter;
import lombok.Setter;

/**
 * @author gongshuiwen
 */
@Getter
@Setter
public abstract class BaseTreeModel<T extends BaseTreeModel<?>> extends BaseModel {

    @Schema(description = "父级路径")
    @Null(groups = {CreateValidationGroup.class, UpdateValidationGroup.class})
    private String parentPath;

    @Schema(description = "父级ID")
    @OnDelete(OnDelete.Type.CASCADE)
    private Many2One<T> parentId;

    @Schema(description = "子级列表")
    @TableField(exist = false)
    @InverseField("parentId")
    private One2Many<T> children;
}
