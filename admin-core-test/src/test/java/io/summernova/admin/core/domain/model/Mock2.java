package io.summernova.admin.core.domain.model;

import io.summernova.admin.common.validation.CreateValidationGroup;
import io.summernova.admin.common.validation.NullOrNotBlank;
import io.summernova.admin.common.validation.UpdateValidationGroup;
import io.summernova.admin.core.domain.annotations.Many2OneField;
import io.summernova.admin.core.domain.annotations.OnDeleteType;
import io.summernova.admin.core.domain.field.Many2One;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author gongshuiwen
 */
@Getter
@Setter
@NoArgsConstructor
public class Mock2 extends BaseModel {

    @NotBlank(groups = CreateValidationGroup.class)
    @NullOrNotBlank(groups = UpdateValidationGroup.class)
    private String name;

    @Many2OneField(onDelete = OnDeleteType.RESTRICT)
    private Many2One<Mock1> mock1Id1;

    @Many2OneField(onDelete = OnDeleteType.CASCADE)
    private Many2One<Mock1> mock1Id2;

    @Many2OneField(onDelete = OnDeleteType.SET_NULL)
    private Many2One<Mock1> mock1Id3;

    public Mock2(String name) {
        this.name = name;
    }
}
