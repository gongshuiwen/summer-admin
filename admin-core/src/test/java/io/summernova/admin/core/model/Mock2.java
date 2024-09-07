package io.summernova.admin.core.model;

import io.summernova.admin.common.validation.CreateValidationGroup;
import io.summernova.admin.common.validation.NullOrNotBlank;
import io.summernova.admin.common.validation.UpdateValidationGroup;
import io.summernova.admin.core.field.Many2One;
import io.summernova.admin.core.field.annotations.OnDelete;
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

    @OnDelete(OnDelete.Type.RESTRICT)
    private Many2One<Mock1> mock1Id1;

    @OnDelete(OnDelete.Type.CASCADE)
    private Many2One<Mock1> mock1Id2;

    @OnDelete(OnDelete.Type.SET_NULL)
    private Many2One<Mock1> mock1Id3;

    public Mock2(String name) {
        this.name = name;
    }
}
