package com.hzboiler.core.controller;

import com.hzboiler.core.annotaion.WithMockAdmin;
import com.hzboiler.core.entity.Mock;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.hzboiler.core.exception.CoreBusinessExceptionEnums.*;

/**
 * @author gongshuiwen
 */
@Sql(scripts = {"/sql/test/ddl/mock.sql"})
class TestBaseControllerCreate extends MockControllerTestBase {

    static final List<Mock> MOCKS = List.of(new Mock("mock1"), new Mock("mock2"));

    ResultActions doCreate(List<Mock> mocks) throws Exception {
        return mockMvc.perform(
                MockMvcRequestBuilders
                        .post(MOCK_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(mocks)));
    }

    @Test
    @WithAnonymousUser
    void testAnonymous() throws Exception {
        checkResultActionsException(doCreate(MOCKS), ERROR_AUTHENTICATION_FAILED);
    }

    @Test
    @WithMockUser
    void testNotAuthorized() throws Exception {
        checkResultActionsException(doCreate(MOCKS), ERROR_ACCESS_DENIED);
    }

    @Test
    @WithMockUser(authorities = MOCK_AUTHORITY_CREATE)
    void testAuthorized() throws Exception {
        Assertions.assertEquals(0, mockMapper.selectCount(null));

        ResultActions resultActions = doCreate(MOCKS);
        checkResultActionsSuccess(resultActions);
        resultActions
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id", Is.is("1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].name", Is.is(MOCKS.get(0).getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].id", Is.is("2")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].name", Is.is(MOCKS.get(1).getName())))
        ;

        Assertions.assertEquals(2, mockMapper.selectCount(null));

        Mock mock1 = mockMapper.selectById(1);
        Assertions.assertEquals(MOCKS.get(0).getName(), mock1.getName());
        Assertions.assertEquals(0, mock1.getCreateUser());
        Assertions.assertEquals(0, mock1.getUpdateUser());

        Mock mock2 = mockMapper.selectById(2);
        Assertions.assertEquals(MOCKS.get(1).getName(), mock2.getName());
        Assertions.assertEquals(0, mock2.getCreateUser());
        Assertions.assertEquals(0, mock2.getUpdateUser());
    }

    @Test
    @WithMockAdmin
    void testAdmin() throws Exception {
        testAuthorized();
    }

    @Test
    @WithMockAdmin
    void testEmptyList() throws Exception {
        checkResultActionsException(doCreate(new ArrayList<>()), ERROR_INVALID_ARGUMENTS);
    }

    @Test
    @WithMockAdmin
    void testNameNull() throws Exception {
        checkResultActionsException(doCreate(List.of(new Mock(null))), ERROR_INVALID_ARGUMENTS);
    }

    @Test
    @WithMockAdmin
    void testNameEmpty() throws Exception {
        checkResultActionsException(doCreate(List.of(new Mock(""))), ERROR_INVALID_ARGUMENTS);
    }

    @Test
    @WithMockAdmin
    void testNameBlank() throws Exception {
        checkResultActionsException(doCreate(List.of(new Mock("   "))), ERROR_INVALID_ARGUMENTS);
    }

    @Test
    @WithMockAdmin
    void testIdNotNull() throws Exception {
        Mock mock = new Mock("mock");
        mock.setId(1L);
        checkResultActionsException(doCreate(List.of(mock)), ERROR_INVALID_ARGUMENTS);
    }

    @Test
    @WithMockAdmin
    void testCreateTimeNotNull() throws Exception {
        Mock mock = new Mock("mock");
        mock.setCreateTime(LocalDateTime.now());
        checkResultActionsException(doCreate(List.of(mock)), ERROR_INVALID_ARGUMENTS);
    }

    @Test
    @WithMockAdmin
    void testUpdateTimeNotNull() throws Exception {
        Mock mock = new Mock("mock");
        mock.setUpdateTime(LocalDateTime.now());
        checkResultActionsException(doCreate(List.of(mock)), ERROR_INVALID_ARGUMENTS);
    }

    @Test
    @WithMockAdmin
    void testCreateUserNotNull() throws Exception {
        Mock mock = new Mock("mock");
        mock.setCreateUser(1L);
        checkResultActionsException(doCreate(List.of(mock)), ERROR_INVALID_ARGUMENTS);
    }

    @Test
    @WithMockAdmin
    void testUpdateUserNotNull() throws Exception {
        Mock mock = new Mock("mock");
        mock.setUpdateUser(1L);
        checkResultActionsException(doCreate(List.of(mock)), ERROR_INVALID_ARGUMENTS);
    }
}
