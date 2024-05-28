package com.hzboiler.erp.core.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.hzboiler.erp.core.field.Many2Many;
import com.hzboiler.erp.core.model.BaseModel;
import com.hzboiler.erp.core.validation.CreateValidationGroup;
import com.hzboiler.erp.core.validation.NullOrNotBlank;
import com.hzboiler.erp.core.validation.UpdateValidationGroup;
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
