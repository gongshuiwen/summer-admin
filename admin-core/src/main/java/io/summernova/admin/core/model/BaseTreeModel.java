package io.summernova.admin.core.model;

import com.baomidou.mybatisplus.annotation.TableField;
import io.summernova.admin.common.validation.CreateValidationGroup;
import io.summernova.admin.common.validation.UpdateValidationGroup;
import io.summernova.admin.core.field.Many2One;
import io.summernova.admin.core.field.One2Many;
import io.summernova.admin.core.field.annotations.Many2OneField;
import io.summernova.admin.core.field.annotations.OnDeleteType;
import io.summernova.admin.core.field.annotations.One2ManyField;
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
    @Many2OneField(onDelete = OnDeleteType.CASCADE)
    private Many2One<T> parentId;

    @Schema(description = "子级列表")
    @TableField(exist = false)
    @One2ManyField(inverseField = "parentId")
    private One2Many<T> children;
}
