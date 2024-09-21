package io.summernova.admin.module.base.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.summernova.admin.common.validation.CreateValidationGroup;
import io.summernova.admin.common.validation.NullOrNotBlank;
import io.summernova.admin.core.context.supplier.GrantedAuthoritiesServiceSupplier;
import io.summernova.admin.core.field.Many2Many;
import io.summernova.admin.core.field.Many2One;
import io.summernova.admin.core.field.annotations.Many2ManyField;
import io.summernova.admin.core.field.annotations.OnDelete;
import io.summernova.admin.core.jackson2.AllowedForAdmin;
import io.summernova.admin.core.model.BaseModel;
import io.summernova.admin.core.security.account.BaseUser;
import io.summernova.admin.core.security.authorization.GrantedAuthoritiesService;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Set;

/**
 * @author gongshuiwen
 */
@Getter
@Setter
@Schema(description = "用户信息")
public class User extends BaseModel implements BaseUser {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "用户名")
    @NotNull(groups = {CreateValidationGroup.class})
    @NullOrNotBlank
    @Size(min = 2, max = 18)
    private String username;

    @Schema(description = "昵称")
    @NotNull(groups = {CreateValidationGroup.class})
    @NullOrNotBlank
    @Size(min = 2, max = 18)
    private String nickname;

    @Schema(description = "密码")
    @NotNull(groups = {CreateValidationGroup.class})
    @NullOrNotBlank
    @Size(min = 6, max = 18)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Schema(description = "邮箱")
    @Email(message = "邮箱格式不正确")
    @Size(max = 50, message = "邮箱长度不能超过50个字符")
    private String email;

    @Schema(description = "手机号码")
    @Size(max = 11, message = "手机号码长度不能超过11个字符")
    private String phone;

    @Schema(description = "用户性别 0=未知,1=女,2=男")
    @Min(value = 0, message = "性别不正确")
    @Max(value = 2, message = "性别不正确")
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
    @Many2ManyField(sourceField = "user_id", targetField = "role_id", joinTable = "user_role")
    private Many2Many<Role> roles;

    @TableField(exist = false)
    private Set<? extends GrantedAuthority> authorities;

    @Override
    public Set<? extends GrantedAuthority> getAuthorities() {
        if (authorities == null) {
            GrantedAuthoritiesService grantedAuthoritiesService = GrantedAuthoritiesServiceSupplier.getGrantedAuthoritiesService();

            // Get authorities by GrantedAuthoritiesService, always wrapped with unmodifiable set
            authorities = Collections.unmodifiableSet(grantedAuthoritiesService.getAuthoritiesByUserId(getId()));
        }
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @JsonIgnore
    public boolean isEnabled() {
        return status == 1;
    }

    @Override
    public String getDisplayName() {
        return getNickname();
    }
}
