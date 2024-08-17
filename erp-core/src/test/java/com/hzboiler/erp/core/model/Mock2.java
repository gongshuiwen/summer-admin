package com.hzboiler.erp.core.model;

import com.hzboiler.erp.common.validation.CreateValidationGroup;
import com.hzboiler.erp.common.validation.NullOrNotBlank;
import com.hzboiler.erp.common.validation.UpdateValidationGroup;
import com.hzboiler.erp.core.field.Many2One;
import com.hzboiler.erp.core.field.annotations.OnDelete;
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
