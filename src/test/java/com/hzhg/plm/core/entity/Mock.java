package com.hzhg.plm.core.entity;

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
public class Mock extends BaseEntity {

    @NotBlank(groups = CreateValidationGroup.class)
    @NullOrNotBlank(groups = UpdateValidationGroup.class)
    private String name;

    public Mock(String name) {
        this.name = name;
    }

    @Override
    public String getDisplayName() {
        return getName();
    }
}
