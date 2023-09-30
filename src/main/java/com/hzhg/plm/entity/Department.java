package com.hzhg.plm.entity;

import com.hzhg.plm.common.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Schema(description = "部门信息")
public class Department extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "部门ID")
    private Long id;

    @Schema(description = "部门名称")
    private String name;

    @Schema(description = "父部门ID")
    private Long parentId;

    @Schema(description = "父部门名称")
    private String parentName;

    @Schema(description = "显示顺序")
    private Integer orderNum;

    @Schema(description = "部门状态 0=正常,1=停用")
    private String status;

    @Schema(description = "子部门列表")
    private List<Department> children = new ArrayList<>();

    @NotBlank(message = "部门名称不能为空")
    @Size(max = 30, message = "部门名称长度不能超过30个字符")
    public String getName() {
        return name;
    }

    @NotNull(message = "显示顺序不能为空")
    public Integer getOrderNum() {
        return orderNum;
    }
}
