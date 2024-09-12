package io.summernova.admin.spring.web;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * @author gongshuiwen
 */
@Sql(scripts = {"/sql/test/ddl/mock.sql", "/sql/test/data/mock.sql"})
class TestCommonControllerUpdate extends CommonControllerTestBase {

    ResultActions doUpdate(List<Mock> mocks) throws Exception {
        return mockMvc.perform(
                MockMvcRequestBuilders
                        .put(MOCK_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(mocks)));
    }

    @Override
    ResultActions doExample() throws Exception {
        return doUpdate(List.of(Mock.of(1L, "mock11")));
    }

    @Test
    @WithMockUser(authorities = MOCK_AUTHORITY_UPDATE)
    void testAuthorized() throws Exception {
        String updateName = "mock";
        Mock mock1 = Mock.of(1L, updateName);
        Mock mock2 = Mock.of(2L, updateName);

        List<Long> updateIds = Arrays.asList(1L, 2L);
        updateIds.forEach(id -> Assertions.assertNotEquals(updateName, mockService.getBaseMapper().selectById(id).getName()));

        ResultActions resultActions = doUpdate(List.of(mock1, mock2));

        checkResultActionsSuccess(resultActions, true);
        for (Long updateId : updateIds) {
            Mock mock = mockService.getBaseMapper().selectById(updateId);
            Assertions.assertEquals(updateName, mock.getName());
            Assertions.assertEquals(0, mock.getCreateUser());
            Assertions.assertEquals(0, mock.getUpdateUser());
        }
    }

    @Test
    @WithMockAdmin
    void testEmpty() throws Exception {
        checkResultActionsInvalidArguments(doUpdate(List.of()));
    }

    @Test
    @WithMockAdmin
    void testIdNull() throws Exception {
        checkResultActionsInvalidArguments(doUpdate(List.of(new Mock("mock"))));
    }

    @Test
    @WithMockAdmin
    void testNameNull() throws Exception {
        String oldName = mockService.getBaseMapper().selectById(1L).getName();

        ResultActions resultActions = doUpdate(List.of(Mock.of(1L, null)));
        checkResultActionsSuccess(resultActions, true);

        Assertions.assertEquals(oldName, mockService.getBaseMapper().selectById(1L).getName());
    }

    @Test
    @WithMockAdmin
    void testNameEmpty() throws Exception {
        checkResultActionsInvalidArguments(doUpdate(List.of(Mock.of(1L, ""))));
    }

    @Test
    @WithMockAdmin
    void testNameBlank() throws Exception {
        checkResultActionsInvalidArguments(doUpdate(List.of(Mock.of(1L, "   "))));
    }

    @Test
    @WithMockAdmin
    void testCreateTimeNotNull() throws Exception {
        Mock mock = Mock.of(1L, "mock");
        mock.setCreateTime(LocalDateTime.now());
        checkResultActionsInvalidArguments(doUpdate(List.of(mock)));
    }

    @Test
    @WithMockAdmin
    void testUpdateTimeNotNull() throws Exception {
        Mock mock = Mock.of(1L, "mock");
        mock.setUpdateTime(LocalDateTime.now());
        checkResultActionsInvalidArguments(doUpdate(List.of(mock)));
    }

    @Test
    @WithMockAdmin
    void testCreateUserNotNull() throws Exception {
        Mock mock = Mock.of(1L, "mock");
        mock.setCreateUser(1L);
        checkResultActionsInvalidArguments(doUpdate(List.of(mock)));
    }

    @Test
    @WithMockAdmin
    void testUpdateUserNotNull() throws Exception {
        Mock mock = Mock.of(1L, "mock");
        mock.setUpdateUser(1L);
        checkResultActionsInvalidArguments(doUpdate(List.of(mock)));
    }

    @Test
    @WithMockAdmin
    void testReadOnlyNotNull() throws Exception {
        Mock mock = Mock.of(1L, "mock");
        mock.setUpdateUser(1L);
        checkResultActionsInvalidArguments(doUpdate(List.of(mock)));
    }
}
