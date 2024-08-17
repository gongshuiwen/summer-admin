package com.hzboiler.erp.core.model;

import com.hzboiler.erp.common.validation.CreateValidationGroup;
import com.hzboiler.erp.common.validation.NullOrNotBlank;
import com.hzboiler.erp.common.validation.UpdateValidationGroup;
import com.hzboiler.erp.core.field.annotations.ReadOnly;
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
public class Mock extends BaseModel {

    @NotBlank(groups = CreateValidationGroup.class)
    @NullOrNotBlank(groups = UpdateValidationGroup.class)
    private String name;

    @ReadOnly
    private String readOnly;

    public Mock(String name) {
        this.name = name;
    }

    public static Mock of(Long id, String name) {
        Mock mock = new Mock(name);
        mock.setId(id);
        return mock;
    }
}
