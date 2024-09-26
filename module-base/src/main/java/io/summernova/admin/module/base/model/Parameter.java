package io.summernova.admin.module.base.model;

import io.summernova.admin.common.validation.CreateValidationGroup;
import io.summernova.admin.common.validation.NullOrNotBlank;
import io.summernova.admin.core.domain.model.BaseModel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

/**
 * @author gongshuiwen
 */
@Getter
@Setter
@Schema(description = "系统参数")
public class Parameter extends BaseModel {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "键")
    @NotNull(groups = {CreateValidationGroup.class})
    @NullOrNotBlank
    @Size(min = 1, max = 50)
    private String key;

    @Schema(description = "值")
    @NotNull(groups = {CreateValidationGroup.class})
    @NullOrNotBlank
    @Size(max = 65536)
    private String value;

    @Schema(description = "状态 1=正常,0=停用")
    private Integer status;
}
