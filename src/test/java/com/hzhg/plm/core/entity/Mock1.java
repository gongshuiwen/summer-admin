package com.hzhg.plm.core.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.hzhg.plm.core.fields.Many2Many;
import com.hzhg.plm.core.fields.One2Many;
import com.hzhg.plm.core.fields.annotations.InverseField;
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
public class Mock1 extends BaseEntity {

    @NotBlank(groups = CreateValidationGroup.class)
    @NullOrNotBlank(groups = UpdateValidationGroup.class)
    private String name;

    @TableField(exist = false)
    @InverseField("mock1Id")
    private One2Many<Mock2> mock2s;

    @TableField(exist = false)
    private Many2Many<Mock3> mock3s;

    public Mock1(String name) {
        this.name = name;
    }
}
