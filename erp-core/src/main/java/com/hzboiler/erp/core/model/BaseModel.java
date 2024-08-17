package com.hzboiler.erp.core.model;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hzboiler.erp.common.validation.CreateValidationGroup;
import com.hzboiler.erp.common.validation.UpdateValidationGroup;
import com.hzboiler.erp.core.field.annotations.ReadOnly;
import com.hzboiler.erp.core.jackson2.SecurityBeanPropertyFilter;
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
@JsonFilter(SecurityBeanPropertyFilter.FILTER_ID)
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class BaseModel implements Serializable {

    @Schema(description = "ID")
    @TableId(type = IdType.AUTO)
    @Null(groups = {CreateValidationGroup.class})
    @NotNull(groups = {UpdateValidationGroup.class})
    private Long id;

    @Schema(description = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    @ReadOnly
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ReadOnly
    private LocalDateTime updateTime;

    @Schema(description = "创建用户")
    @TableField(fill = FieldFill.INSERT)
    @ReadOnly
    private Long createUser;

    @Schema(description = "更新用户")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ReadOnly
    private Long updateUser;

    public String getName() {
        return "";
    }

    public String getDisplayName() {
        return getName();
    }

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
