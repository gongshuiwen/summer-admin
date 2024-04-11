package com.hzhg.plm.core.entity;

import com.hzhg.plm.core.fields.Many2One;
import com.hzhg.plm.core.fields.annotations.OnDelete;
import com.hzhg.plm.core.validation.CreateValidationGroup;
import com.hzhg.plm.core.validation.NullOrNotBlank;
import com.hzhg.plm.core.validation.UpdateValidationGroup;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Mock2 extends BaseEntity {

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
