package io.summernova.admin.core.model;

import io.summernova.admin.common.validation.CreateValidationGroup;
import io.summernova.admin.common.validation.NullOrNotBlank;
import io.summernova.admin.common.validation.UpdateValidationGroup;
import io.summernova.admin.core.field.annotations.ReadOnly;
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
