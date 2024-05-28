package com.hzboiler.core.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.hzboiler.core.field.Many2Many;
import com.hzboiler.core.field.One2Many;
import com.hzboiler.core.field.annotations.InverseField;
import com.hzboiler.core.validation.CreateValidationGroup;
import com.hzboiler.core.validation.NullOrNotBlank;
import com.hzboiler.core.validation.UpdateValidationGroup;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Mock1 extends BaseModel {

    @NotBlank(groups = CreateValidationGroup.class)
    @NullOrNotBlank(groups = UpdateValidationGroup.class)
    private String name;

    @TableField(exist = false)
    @InverseField("mock1Id1")
    private One2Many<Mock2> mock2s1;

    @TableField(exist = false)
    @InverseField("mock1Id2")
    private One2Many<Mock2> mock2s2;

    @TableField(exist = false)
    @InverseField("mock1Id3")
    private One2Many<Mock2> mock2s3;

    @TableField(exist = false)
    private Many2Many<Mock3> mock3s;

    public Mock1(String name) {
        this.name = name;
    }
}
