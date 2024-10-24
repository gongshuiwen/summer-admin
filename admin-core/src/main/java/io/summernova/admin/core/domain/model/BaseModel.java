package io.summernova.admin.core.domain.model;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.summernova.admin.common.validation.CreateValidationGroup;
import io.summernova.admin.common.validation.UpdateValidationGroup;
import io.summernova.admin.core.domain.annotations.ReadOnly;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author gongshuiwen
 */
@Getter
@Setter
public abstract class BaseModel implements Serializable {

    @Schema(description = "ID")
    @TableId(type = IdType.AUTO)
    @Null(groups = {CreateValidationGroup.class})
    @NotNull(groups = {UpdateValidationGroup.class})
    private Long id;

    @Schema(description = "Create Time")
    @TableField(fill = FieldFill.INSERT)
    @ReadOnly
    private LocalDateTime createTime;

    @Schema(description = "Update Time")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ReadOnly
    private LocalDateTime updateTime;

    @Schema(description = "Create User")
    @TableField(fill = FieldFill.INSERT)
    @ReadOnly
    private Long createUser;

    @Schema(description = "Update User")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ReadOnly
    private Long updateUser;

    public String getName() {
        return "";
    }

    public String getDisplayName() {
        return getName();
    }

    // -----------------------
    // inherited from Object
    // -----------------------
    @Override
    public String toString() {
        return getClass().getName() + "@" + getId() + "[" + getDisplayName() + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (o instanceof BaseModel o1)
            return Objects.equals(id, o1.id);
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
