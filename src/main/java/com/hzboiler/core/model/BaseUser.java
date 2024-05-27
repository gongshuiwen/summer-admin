package com.hzboiler.core.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Set;

/**
 * @author gongshuiwen
 */
@Setter
public abstract class BaseUser extends BaseModel implements Serializable, UserDetails {

    @TableField(exist = false)
    private Set<GrantedAuthority> authorities;

    @Override
    public Set<GrantedAuthority> getAuthorities() {
        if (authorities == null) {
            return Set.of();
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
        return true;
    }
}
