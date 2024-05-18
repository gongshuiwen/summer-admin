package com.hzboiler.base.model;


import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hzboiler.core.entity.BaseUser;
import com.hzboiler.core.jackson2.AllowedForAdmin;
import com.hzboiler.core.fields.Many2Many;
import com.hzboiler.core.fields.Many2One;
import com.hzboiler.core.fields.annotations.OnDelete;
import com.hzboiler.core.validation.CreateValidationGroup;
import com.hzboiler.core.validation.NullOrNotBlank;
import com.hzboiler.core.validation.UpdateValidationGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static com.hzboiler.core.utils.Constants.ROLE_PREFIX;

@Getter
@Setter
@Schema(description = "用户信息")
public class User extends BaseUser implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "用户名")
    @NotNull(groups = {CreateValidationGroup.class})
    @NullOrNotBlank(groups = {CreateValidationGroup.class, UpdateValidationGroup.class})
    @Size(min = 2, max = 18, groups = {CreateValidationGroup.class, UpdateValidationGroup.class})
    private String username;

    @Schema(description = "昵称")
    @NotNull(groups = {CreateValidationGroup.class})
    @NullOrNotBlank(groups = {CreateValidationGroup.class, UpdateValidationGroup.class})
    @Size(min = 2, max = 18, groups = {CreateValidationGroup.class, UpdateValidationGroup.class})
    private String nickname;

    @Schema(description = "密码")
    @NotNull(groups = {CreateValidationGroup.class})
    @NullOrNotBlank(groups = {CreateValidationGroup.class, UpdateValidationGroup.class})
    @Size(min = 6, max = 18, groups = {CreateValidationGroup.class, UpdateValidationGroup.class})
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Schema(description = "邮箱")
    @Email(groups = {CreateValidationGroup.class, UpdateValidationGroup.class},
            message = "邮箱格式不正确")
    @Size(max = 50, groups = {CreateValidationGroup.class, UpdateValidationGroup.class},
            message = "邮箱长度不能超过50个字符")
    private String email;

    @Schema(description = "手机号码")
    @Size(max = 11, groups = {CreateValidationGroup.class, UpdateValidationGroup.class},
            message = "手机号码长度不能超过11个字符")
    private String phone;

    @Schema(description = "用户性别 0=未知,1=女,2=男")
    @Min(value = 0, groups = {CreateValidationGroup.class, UpdateValidationGroup.class}, message = "性别不正确")
    @Max(value = 2, groups = {CreateValidationGroup.class, UpdateValidationGroup.class}, message = "性别不正确")
    private Integer sex;

    @Schema(description = "用户头像")
    private String avatar;

    @Schema(description = "状态 1=正常,0=停用")
    @AllowedForAdmin
    private Integer status;

    @Schema(description = "最后登录IP")
    @AllowedForAdmin
    private String loginIp;

    @Schema(description = "最后登录时间")
    @AllowedForAdmin
    private LocalDateTime loginTime;

    @Schema(description = "部门")
    @OnDelete(OnDelete.Type.RESTRICT)
    private Many2One<Department> departmentId;

    @Schema(description = "角色")
    @TableField(exist = false)
    private Many2Many<Role> roles;

    public void addAuthoritiesWithRolesAndPermissions(Collection<Role> roles, Collection<Permission> permissions) {
        Set<GrantedAuthority> authorities = new HashSet<>();
        roles.stream()
                .map(role -> new SimpleGrantedAuthority(ROLE_PREFIX + role.getCode()))
                .forEach(authorities::add);
        permissions.stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getCode()))
                .forEach(authorities::add);
        setAuthorities(Collections.unmodifiableSet(authorities));
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return status == 1;
    }

    @Override
    public String getDisplayName() {
        return getNickname();
    }
}
