package com.hzhg.plm.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hzhg.plm.core.annotations.AllowedForRoles;
import com.hzhg.plm.core.annotations.FetchName;
import com.hzhg.plm.core.entity.BaseEntity;
import com.hzhg.plm.core.validation.CreateValidationGroup;
import com.hzhg.plm.core.validation.NullOrNotBlank;
import com.hzhg.plm.core.validation.UpdateValidationGroup;
import com.hzhg.plm.mapper.DepartmentMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static com.hzhg.plm.core.security.DataAccessAuthorityChecker.ROLE_ADMIN;

@Getter
@Setter
@Schema(description = "用户信息")
public class User extends BaseEntity implements Serializable, UserDetails {

    @Serial
    private static final long serialVersionUID = 1L;

    private static final String ROLE_PREFIX = "ROLE_";

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
    @AllowedForRoles(value = {ROLE_ADMIN})
    private Integer status;

    @Schema(description = "最后登录IP")
    @AllowedForRoles(value = {ROLE_ADMIN})
    private String loginIp;

    @Schema(description = "最后登录时间")
    @AllowedForRoles(value = {ROLE_ADMIN})
    private LocalDateTime loginTime;

    @Schema(description = "部门ID")
    private Long departmentId;

    @TableField(exist = false)
    @Schema(description = "部门名称")
    @FetchName(idField = "departmentId", mapper = DepartmentMapper.class)
    private String departmentName;

    @TableField(exist = false)
    private Set<Role> roles;

    @TableField(exist = false)
    private Set<GrantedAuthority> authorities;

    public void addAuthoritiesWithRoles(Set<Role> roles) {
        if (authorities == null) {
            authorities = new HashSet<>();
        }
        roles.stream()
                .map(role -> new SimpleGrantedAuthority(ROLE_PREFIX + role.getCode()))
                .forEach(authorities::add);
    }

    public void addAuthoritiesWithPermissions(Set<Permission> permissions) {
        if (authorities == null) {
            authorities = new HashSet<>();
        }
        permissions.stream()
                .map(perm -> new SimpleGrantedAuthority(perm.getCode()))
                .forEach(authorities::add);
    }

    @Override
    public Set<GrantedAuthority> getAuthorities() {
        if (authorities == null) {
            return new HashSet<>();
        }
        return authorities;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
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
