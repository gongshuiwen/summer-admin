package io.summernova.admin.core.jackson2;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import io.summernova.admin.core.annotaion.WithMockUser;
import io.summernova.admin.core.context.BaseContextExtension;
import io.summernova.admin.core.domain.model.BaseModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static io.summernova.admin.core.jackson2.SecurityBeanPropertyFilter.FILTER_ID;
import static io.summernova.admin.core.jackson2.SecurityBeanPropertyFilter.INSTANCE;
import static io.summernova.admin.core.security.Constants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author gongshuiwen
 */
@ExtendWith(BaseContextExtension.class)
class TestSecurityBeanPropertyFilter {

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

    @Test
    @WithMockUser(userId = 2L, authorities = {ROLE_SYS_ADMIN})
    void testAllowedForAdmin() throws JsonProcessingException, NoSuchFieldException, IllegalAccessException {
        String result = mapper.writeValueAsString(mock);
        assertEquals("{\"allowedForAdmin\":true,\"allowedForAdminOrRoles\":true}", result);
    }

    @Test
    @WithMockUser(userId = 2L, authorities = {ROLE_BASE_USER})
    void testAllowedForRoles() throws JsonProcessingException, NoSuchFieldException, IllegalAccessException {
        String result = mapper.writeValueAsString(mock);
        assertEquals("{\"allowedForRoles\":true,\"allowedForAdminOrRoles\":true}", result);
    }

    @Test
    @WithMockUser(userId = 2L)
    void testAllowedForCreateUser() throws JsonProcessingException, NoSuchFieldException, IllegalAccessException {
        mock.setCreateUser(2L);
        String result = mapper.writeValueAsString(mock);
        assertEquals("{\"createUser\":2,\"allowedForCreateUser\":true,\"allowedForCreateOrUpdateUser\":true}", result);
    }

    @Test
    @WithMockUser(userId = 2L)
    void testAllowedForUpdateUser() throws JsonProcessingException, NoSuchFieldException, IllegalAccessException {
        mock.setUpdateUser(2L);
        String result = mapper.writeValueAsString(mock);
        assertEquals("{\"updateUser\":2,\"allowedForUpdateUser\":true,\"allowedForCreateOrUpdateUser\":true}", result);
    }

    @Test
    @WithMockUser(userId = 2L, authorities = {ROLE_BASE_USER})
    void testAllowedForRolesAndCreateUser() throws JsonProcessingException, NoSuchFieldException, IllegalAccessException {
        mock.setCreateUser(2L);
        String result = mapper.writeValueAsString(mock);
        assertEquals("{\"createUser\":2,\"allowedForRoles\":true,\"allowedForAdminOrRoles\":true,\"allowedForCreateUser\":true,\"allowedForCreateOrUpdateUser\":true,\"allowedForRolesAndCreateUser\":true}", result);
    }

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
}
