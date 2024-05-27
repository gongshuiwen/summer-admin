package com.hzboiler.core.controller;

import com.hzboiler.core.annotaion.WithMockAdmin;
import com.hzboiler.core.model.Mock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static com.hzboiler.core.exception.CoreBusinessExceptionEnums.*;

/**
 * @author gongshuiwen
 */
@Sql(scripts = {"/sql/test/ddl/mock.sql", "/sql/test/data/mock.sql"})
class TestBaseControllerUpdate extends MockControllerTestBase {

    ResultActions doUpdate(List<Mock> mocks) throws Exception {
        return mockMvc.perform(
                MockMvcRequestBuilders
                        .put(MOCK_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(mocks)));
    }

    @Test
    @WithAnonymousUser
    void testAnonymous() throws Exception {
        checkResultActionsException(doUpdate(List.of(
                Mock.of(1L, "mock11"),
                Mock.of(2L, "mock22")
        )), ERROR_AUTHENTICATION_FAILED);
    }

    @Test
    @WithMockUser
    void testNotAuthorized() throws Exception {
        checkResultActionsException(doUpdate(List.of(
                Mock.of(1L, "mock11"),
                Mock.of(2L, "mock22")
        )), ERROR_ACCESS_DENIED);
    }

    @Test
    @WithMockUser(authorities = MOCK_AUTHORITY_UPDATE)
    void testAuthorized() throws Exception {
        String updateName = "mock";
        Mock mock1 = Mock.of(1L, updateName);
        Mock mock2 = Mock.of(2L, updateName);

        List<Long> updateIds = Arrays.asList(1L, 2L);
        updateIds.forEach(id -> Assertions.assertNotEquals(updateName, mockMapper.selectById(id).getName()));

        ResultActions resultActions = doUpdate(List.of(mock1, mock2));

        checkResultActionsSuccess(resultActions);
        for (Long updateId : updateIds) {
            Mock mock = mockMapper.selectById(updateId);
            Assertions.assertEquals(updateName, mock.getName());
            Assertions.assertEquals(0, mock.getCreateUser());
            Assertions.assertEquals(0, mock.getUpdateUser());
        }
    }

    @Test
    @WithMockAdmin
    void testAdmin() throws Exception {
        testAuthorized();
    }

    @Test
    @WithMockAdmin
    void testEmpty() throws Exception {
        checkResultActionsException(doUpdate(List.of()), ERROR_INVALID_ARGUMENTS);
    }

    @Test
    @WithMockAdmin
    void testIdNull() throws Exception {
        checkResultActionsException(doUpdate(List.of(new Mock("mock"))), ERROR_INVALID_ARGUMENTS);
    }

    @Test
    @WithMockAdmin
    void testNameNull() throws Exception {
        String oldName = mockMapper.selectById(1L).getName();

        ResultActions resultActions = doUpdate(List.of(Mock.of(1L, null)));
        checkResultActionsSuccess(resultActions);

        Assertions.assertEquals(oldName, mockMapper.selectById(1L).getName());
    }

    @Test
    @WithMockAdmin
    void testNameEmpty() throws Exception {
        checkResultActionsException(doUpdate(List.of(Mock.of(1L, ""))), ERROR_INVALID_ARGUMENTS);
    }

    @Test
    @WithMockAdmin
    void testNameBlank() throws Exception {
        checkResultActionsException(doUpdate(List.of(Mock.of(1L, "   "))), ERROR_INVALID_ARGUMENTS);
    }

    @Test
    @WithMockAdmin
    void testCreateTimeNotNull() throws Exception {
        Mock mock = Mock.of(1L, "mock");
        mock.setCreateTime(LocalDateTime.now());
        checkResultActionsException(doUpdate(List.of(mock)), ERROR_INVALID_ARGUMENTS);
    }

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql"})
    @WithMockAdmin
    void testUpdateTimeNotNull() throws Exception {
        Mock mock = Mock.of(1L, "mock");
        mock.setUpdateTime(LocalDateTime.now());
        checkResultActionsException(doUpdate(List.of(mock)), ERROR_INVALID_ARGUMENTS);
    }

    @Test
    @WithMockAdmin
    void testCreateUserNotNull() throws Exception {
        Mock mock = Mock.of(1L, "mock");
        mock.setCreateUser(1L);
        checkResultActionsException(doUpdate(List.of(mock)), ERROR_INVALID_ARGUMENTS);
    }

    @Test
    @WithMockAdmin
    void testUpdateUserNotNull() throws Exception {
        Mock mock = Mock.of(1L, "mock");
        mock.setUpdateUser(1L);
        checkResultActionsException(doUpdate(List.of(mock)), ERROR_INVALID_ARGUMENTS);
    }
}
