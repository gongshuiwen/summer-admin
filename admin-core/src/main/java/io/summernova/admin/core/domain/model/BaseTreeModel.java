package io.summernova.admin.core.domain.model;

import com.baomidou.mybatisplus.annotation.TableField;
import io.summernova.admin.common.validation.CreateValidationGroup;
import io.summernova.admin.common.validation.UpdateValidationGroup;
import io.summernova.admin.core.domain.field.Many2One;
import io.summernova.admin.core.domain.field.One2Many;
import io.summernova.admin.core.domain.annotations.Many2OneField;
import io.summernova.admin.core.domain.annotations.OnDeleteType;
import io.summernova.admin.core.domain.annotations.One2ManyField;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Null;
import lombok.Getter;
import lombok.Setter;

/**
 * @author gongshuiwen
 */
@Getter
@Setter
public abstract class BaseTreeModel<T extends BaseTreeModel<T>> extends BaseModel {

    @Schema(description = "Parent Path")
    @Null(groups = {CreateValidationGroup.class, UpdateValidationGroup.class})
    private String parentPath;

    @Schema(description = "Parent ID")
    @Many2OneField(onDelete = OnDeleteType.CASCADE)
    private Many2One<T> parentId;

    @Schema(description = "Children List")
    @TableField(exist = false)
    @One2ManyField(inverseField = "parentId")
    private One2Many<T> children;

    // -----------------------
    // Display name
    // -----------------------
    @Override
    public String getDisplayName() {
        return getParentPath();
    }
}
