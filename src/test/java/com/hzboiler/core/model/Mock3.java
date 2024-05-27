package com.hzboiler.core.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.hzboiler.core.field.Many2Many;
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
public class Mock3 extends BaseModel {

    @NotBlank(groups = CreateValidationGroup.class)
    @NullOrNotBlank(groups = UpdateValidationGroup.class)
    private String name;

    @TableField(exist = false)
    Many2Many<Mock1> mock1s;

    public Mock3(String name) {
        this.name = name;
    }
}
