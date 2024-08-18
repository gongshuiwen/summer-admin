package com.hzboiler.erp.core.jackson2;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.hzboiler.erp.core.context.BaseContext;
import com.hzboiler.erp.core.context.BaseContextHolder;
import com.hzboiler.erp.core.context.BaseContextImpl;
import com.hzboiler.erp.core.model.BaseModel;
import com.hzboiler.erp.core.security.account.BaseUser;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.Set;

import static com.hzboiler.erp.core.jackson2.SecurityBeanPropertyFilter.FILTER_ID;
import static com.hzboiler.erp.core.jackson2.SecurityBeanPropertyFilter.INSTANCE;
import static com.hzboiler.erp.core.security.Constants.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author gongshuiwen
 */
class TestSecurityBeanPropertyFilter {

    @Getter
    @Setter
    @NoArgsConstructor
    static class Mock extends BaseModel {

        @AllowedForAdmin
        private Boolean allowedForAdmin;

        @AllowedForRoles(value = {CODE_BASE_USER})
        private Boolean allowedForRoles;

        @AllowedForAdmin
        @AllowedForRoles(value = {CODE_BASE_USER})
        private Boolean allowedForAdminOrRoles;

        @AllowedForCreateUser
        private Boolean allowedForCreateUser;

        @AllowedForUpdateUser
        private Boolean allowedForUpdateUser;

        @AllowedForCreateUser
        @AllowedForUpdateUser
        private Boolean allowedForCreateOrUpdateUser;

        @AllowedForRoles(value = {CODE_BASE_USER})
        @AllowedForCreateUser
        private Boolean allowedForRolesAndCreateUser;

        @Override
        public String getName() {
            return null;
        }
    }

    @Setter
    static class User extends BaseModel implements BaseUser {

        @TableField(exist = false)
        private Set<? extends GrantedAuthority> authorities;

        public User(Long id, Set<GrantedAuthority> authorities) {
            setId(id);
            setAuthorities(authorities);
        }

        @Override
        public Set<? extends GrantedAuthority> getAuthorities() {
            return Objects.requireNonNullElseGet(authorities, Set::of);
        }

        @Override
        public String getPassword() {
            return "";
        }

        @Override
        public String getUsername() {
            return "";
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

        @Override
        public boolean isEnabled() {
            return false;
        }
    }

    static void setBaseContextWithUser(BaseUser user) throws NoSuchFieldException, IllegalAccessException {
        BaseContext baseContext = BaseContextHolder.getContext();
        Field field = BaseContextImpl.class.getDeclaredField("user");
        field.setAccessible(true);
        field.set(baseContext, user);
        field = BaseContextImpl.class.getDeclaredField("userId");
        field.setAccessible(true);
        field.set(baseContext, user.getId());
        field = BaseContextImpl.class.getDeclaredField("authorities");
        field.setAccessible(true);
        field.set(baseContext, user.getAuthorities());
    }

    ObjectMapper mapper;
    {
        mapper = new ObjectMapper();
        mapper.setFilterProvider(new SimpleFilterProvider().addFilter(FILTER_ID, INSTANCE));
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    Mock mock;
    {
        mock = new Mock();
        mock.setAllowedForAdmin(true);
        mock.setAllowedForRoles(true);
        mock.setAllowedForAdminOrRoles(true);
        mock.setAllowedForCreateUser(true);
        mock.setAllowedForUpdateUser(true);
        mock.setAllowedForCreateOrUpdateUser(true);
        mock.setAllowedForRolesAndCreateUser(true);
    }

    Long userId = 2L;
    User userWithRoleSysAdmin = new User(userId, Set.of(GRANTED_AUTHORITY_ROLE_SYS_ADMIN));
    User userWithRoleBaseUser = new User(userId, Set.of(GRANTED_AUTHORITY_ROLE_BASE_USER));
    User userWithRoleEmpty = new User(userId, Set.of());

    @AfterEach
    void afterEach() {
        BaseContextHolder.clearContext();
    }

    @Test
    void testAllowedForAdmin() throws JsonProcessingException, NoSuchFieldException, IllegalAccessException {
        setBaseContextWithUser(userWithRoleSysAdmin);
        String result = mapper.writeValueAsString(mock);
        assertEquals("{\"allowedForAdmin\":true,\"allowedForAdminOrRoles\":true}", result);
    }

    @Test
    void testAllowedForRoles() throws JsonProcessingException, NoSuchFieldException, IllegalAccessException {
        setBaseContextWithUser(userWithRoleBaseUser);
        String result = mapper.writeValueAsString(mock);
        assertEquals("{\"allowedForRoles\":true,\"allowedForAdminOrRoles\":true}", result);
    }

    @Test
    void testAllowedForCreateUser() throws JsonProcessingException, NoSuchFieldException, IllegalAccessException {
        setBaseContextWithUser(userWithRoleEmpty);
        mock.setCreateUser(userId);
        String result = mapper.writeValueAsString(mock);
        assertEquals("{\"createUser\":2,\"allowedForCreateUser\":true,\"allowedForCreateOrUpdateUser\":true}", result);
    }

    @Test
    void testAllowedForUpdateUser() throws JsonProcessingException, NoSuchFieldException, IllegalAccessException {
        setBaseContextWithUser(userWithRoleEmpty);
        mock.setUpdateUser(userId);
        String result = mapper.writeValueAsString(mock);
        assertEquals("{\"updateUser\":2,\"allowedForUpdateUser\":true,\"allowedForCreateOrUpdateUser\":true}", result);
    }

    @Test
    void testAllowedForRolesAndCreateUser() throws JsonProcessingException, NoSuchFieldException, IllegalAccessException {
        setBaseContextWithUser(userWithRoleBaseUser);
        mock.setCreateUser(userId);
        String result = mapper.writeValueAsString(mock);
        assertEquals("{\"createUser\":2,\"allowedForRoles\":true,\"allowedForAdminOrRoles\":true,\"allowedForCreateUser\":true,\"allowedForCreateOrUpdateUser\":true,\"allowedForRolesAndCreateUser\":true}", result);
    }
}
