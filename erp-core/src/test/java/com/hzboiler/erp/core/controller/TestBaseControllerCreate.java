package com.hzboiler.erp.core.controller;

import com.hzboiler.erp.core.annotaion.WithMockAdmin;
import com.hzboiler.erp.core.model.Mock;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

    @Override
    ResultActions doExample() throws Exception {
        return doCreate(MOCKS);
    }

    @Test
    @WithMockUser(authorities = MOCK_AUTHORITY_CREATE)
    void testAuthorized() throws Exception {
        assertEquals(0, mockMapper.selectCount(null));

        ResultActions resultActions = doCreate(MOCKS);
        checkResultActionsSuccess(resultActions);
        resultActions
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id", Is.is("1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].name", Is.is(MOCKS.get(0).getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].id", Is.is("2")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].name", Is.is(MOCKS.get(1).getName())))
        ;

        assertEquals(2, mockMapper.selectCount(null));

        Mock mock1 = mockMapper.selectById(1);
        assertEquals(MOCKS.get(0).getName(), mock1.getName());
        assertEquals(0, mock1.getCreateUser());
        assertEquals(0, mock1.getUpdateUser());

        Mock mock2 = mockMapper.selectById(2);
        assertEquals(MOCKS.get(1).getName(), mock2.getName());
        assertEquals(0, mock2.getCreateUser());
        assertEquals(0, mock2.getUpdateUser());
    }

    @Test
    @WithMockAdmin
    void testEmptyList() throws Exception {
        checkResultActionsInvalidArguments(doCreate(new ArrayList<>()));
    }

    @Test
    @WithMockAdmin
    void testNameNull() throws Exception {
        checkResultActionsInvalidArguments(doCreate(List.of(new Mock(null))));
    }

    @Test
    @WithMockAdmin
    void testNameEmpty() throws Exception {
        checkResultActionsInvalidArguments(doCreate(List.of(new Mock(""))));
    }

    @Test
    @WithMockAdmin
    void testNameBlank() throws Exception {
        checkResultActionsInvalidArguments(doCreate(List.of(new Mock("   "))));
    }

    @Test
    @WithMockAdmin
    void testIdNotNull() throws Exception {
        Mock mock = new Mock("mock");
        mock.setId(1L);
        checkResultActionsInvalidArguments(doCreate(List.of(mock)));
    }

    @Test
    @WithMockAdmin
    void testCreateTimeNotNull() throws Exception {
        Mock mock = new Mock("mock");
        mock.setCreateTime(LocalDateTime.now());
        checkResultActionsInvalidArguments(doCreate(List.of(mock)));
    }

    @Test
    @WithMockAdmin
    void testUpdateTimeNotNull() throws Exception {
        Mock mock = new Mock("mock");
        mock.setUpdateTime(LocalDateTime.now());
        checkResultActionsInvalidArguments(doCreate(List.of(mock)));
    }

    @Test
    @WithMockAdmin
    void testCreateUserNotNull() throws Exception {
        Mock mock = new Mock("mock");
        mock.setCreateUser(1L);
        checkResultActionsInvalidArguments(doCreate(List.of(mock)));
    }

    @Test
    @WithMockAdmin
    void testUpdateUserNotNull() throws Exception {
        Mock mock = new Mock("mock");
        mock.setUpdateUser(1L);
        checkResultActionsInvalidArguments(doCreate(List.of(mock)));
    }

    @Test
    @WithMockAdmin
    void testReadOnlyNotNull() throws Exception {
        Mock mock = new Mock("mock");
        mock.setUpdateUser(1L);
        checkResultActionsInvalidArguments(doCreate(List.of(mock)));
    }
}
