package com.hzhg.plm.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.hzhg.plm.common.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Schema(description = "用户信息")
public class User extends BaseEntity implements Serializable, UserDetails {

    private static final long serialVersionUID = 1L;

    @Schema(description = "用户ID")
    private Long id;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "密码")
    private String password;

    @Schema(description = "姓名")
    private String nickName;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "手机号码")
    private String phone;

    @Schema(description = "用户性别 0=未知,1=女,2=男")
    private Integer sex;

    @Schema(description = "用户头像")
    private String avatar;

    @Schema(description = "帐号状态 0=正常,1=停用")
    private Integer status;

    @Schema(description = "最后登录IP")
    private String loginIp;

    @Schema(description = "最后登录时间")
    private LocalDateTime loginTime;

    @TableField(exist = false)
    private List<Role> roles;

    @TableField(exist = false)
    Set<GrantedAuthority> authorities;

    public boolean isAdmin() {
        return isAdmin(this.id);
    }

    public static boolean isAdmin(Long userId) {
        return userId != null && 1L == userId;
    }

    @Size(max = 30, message = "用户昵称长度不能超过30个字符")
    public String getNickName() {
        return nickName;
    }

    @NotBlank(message = "用户账号不能为空")
    @Size(max = 30, message = "用户账号长度不能超过30个字符")
    public String getUsername() {
        return username;
    }

    @Email(message = "邮箱格式不正确")
    @Size(max = 50, message = "邮箱长度不能超过50个字符")
    public String getEmail() {
        return email;
    }

    @Size(max = 11, message = "手机号码长度不能超过11个字符")
    public String getPhone() {
        return phone;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return status == 1;
    }
}

