package com.hzboiler.core.jackson2;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.hzboiler.core.context.BaseContext;
import com.hzboiler.core.context.BaseContextHolder;
import com.hzboiler.core.entity.BaseEntity;
import com.hzboiler.base.model.Role;
import com.hzboiler.base.model.User;
import com.hzboiler.core.entity.BaseUser;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.junit.jupiter.api.Test;


import java.lang.reflect.Field;
import java.util.List;

import static com.hzboiler.core.jackson2.SecurityBeanPropertyFilter.FILTER_ID;
import static com.hzboiler.core.jackson2.SecurityBeanPropertyFilter.INSTANCE;
import static com.hzboiler.core.utils.Constants.*;
import static org.junit.jupiter.api.Assertions.*;

public class TestSecurityBeanPropertyFilter {

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Mock extends BaseEntity {

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

    private static void setBaseContextWithUser(BaseUser user) throws NoSuchFieldException, IllegalAccessException {
        BaseContext baseContext = BaseContextHolder.getContext();
        Field field = BaseContext.class.getDeclaredField("user");
        field.setAccessible(true);
        field.set(baseContext, user);
        field = BaseContext.class.getDeclaredField("userId");
        field.setAccessible(true);
        field.set(baseContext, user.getId());
        field = BaseContext.class.getDeclaredField("authorities");
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

    Role roleSysAdmin;
    {
        roleSysAdmin = new Role();
        roleSysAdmin.setCode(CODE_SYS_ADMIN);
    }

    Role roleBaseUser;
    {
        roleBaseUser = new Role();
        roleBaseUser.setCode(CODE_BASE_USER);
    }

    User userWithRoleSysAdmin;
    {
        userWithRoleSysAdmin = new User();
        userWithRoleSysAdmin.setId(1L);
        userWithRoleSysAdmin.addAuthoritiesWithRolesAndPermissions(List.of(roleSysAdmin), List.of());
    }

    User userWithRoleBaseUser;
    {
        userWithRoleBaseUser = new User();
        userWithRoleBaseUser.setId(1L);
        userWithRoleBaseUser.addAuthoritiesWithRolesAndPermissions(List.of(roleBaseUser), List.of());
    }

    User userWithRoleEmpty;
    {
        userWithRoleEmpty = new User();
        userWithRoleEmpty.setId(1L);
    }

    @Test
    public void testAllowedForAdmin() throws JsonProcessingException, NoSuchFieldException, IllegalAccessException {
        setBaseContextWithUser(userWithRoleSysAdmin);
        String result = mapper.writeValueAsString(mock);
        assertEquals("{\"allowedForAdmin\":true,\"allowedForAdminOrRoles\":true}", result);
    }

    @Test
    public void testAllowedForRoles() throws JsonProcessingException, NoSuchFieldException, IllegalAccessException {
        setBaseContextWithUser(userWithRoleBaseUser);
        String result = mapper.writeValueAsString(mock);
        assertEquals("{\"allowedForRoles\":true,\"allowedForAdminOrRoles\":true}", result);
    }

    @Test
    public void testAllowedForCreateUser() throws JsonProcessingException, NoSuchFieldException, IllegalAccessException {
        setBaseContextWithUser(userWithRoleEmpty);
        mock.setCreateUser(1L);
        String result = mapper.writeValueAsString(mock);
        assertEquals("{\"createUser\":1,\"allowedForCreateUser\":true,\"allowedForCreateOrUpdateUser\":true}", result);
    }

    @Test
    public void testAllowedForUpdateUser() throws JsonProcessingException, NoSuchFieldException, IllegalAccessException {
        setBaseContextWithUser(userWithRoleEmpty);
        mock.setUpdateUser(1L);
        String result = mapper.writeValueAsString(mock);
        assertEquals("{\"updateUser\":1,\"allowedForUpdateUser\":true,\"allowedForCreateOrUpdateUser\":true}", result);
    }

    @Test void testAllowedForRolesAndCreateUser() throws JsonProcessingException, NoSuchFieldException, IllegalAccessException {
        setBaseContextWithUser(userWithRoleBaseUser);
        mock.setCreateUser(1L);
        String result = mapper.writeValueAsString(mock);
        assertEquals("{\"createUser\":1,\"allowedForRoles\":true,\"allowedForAdminOrRoles\":true,\"allowedForCreateUser\":true,\"allowedForCreateOrUpdateUser\":true,\"allowedForRolesAndCreateUser\":true}", result);
    }
}
