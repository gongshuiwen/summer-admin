package com.hzboiler.core.entity;

import com.hzboiler.core.entity.BaseEntity;
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
public class Mock extends BaseEntity {

    @NotBlank(groups = CreateValidationGroup.class)
    @NullOrNotBlank(groups = UpdateValidationGroup.class)
    private String name;

    public Mock(String name) {
        this.name = name;
    }
}
