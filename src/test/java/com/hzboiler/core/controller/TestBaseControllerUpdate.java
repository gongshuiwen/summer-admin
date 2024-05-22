package com.hzboiler.core.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hzboiler.core.annotaion.WithMockAdmin;
import com.hzboiler.core.entity.Mock;
import com.hzboiler.core.mapper.MockMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static com.hzboiler.core.security.DataAccessAuthority.AUTHORITY_UPDATE;
import static com.hzboiler.core.utils.ResultCheckUtil.*;


@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = {"/sql/test/ddl/mock.sql", "/sql/test/data/mock.sql"})
public class TestBaseControllerUpdate {

    static final String MOCK_PATH = "/mock";
    static final String MOCK_ENTITY_NAME = "Mock";
    static final String MOCK_AUTHORITY_UPDATE = MOCK_ENTITY_NAME + ":" + AUTHORITY_UPDATE;

    MockMvc mockMvc;

    MockMapper mockMapper;

    ObjectMapper objectMapper;

    public TestBaseControllerUpdate(
            @Autowired MockMvc mockMvc,
            @Autowired MockMapper mockMapper,
            @Autowired MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter) {
        this.mockMvc = mockMvc;
        this.mockMapper = mockMapper;
        this.objectMapper = mappingJackson2HttpMessageConverter.getObjectMapper();
    }

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
        ResultActions resultActions = doUpdate(List.of(Mock.of(1L, "mock")));
        checkResultActionsAuthenticationFailed(resultActions);
    }

    @Test
    @WithMockUser
    void testNotAuthorized() throws Exception {
        ResultActions resultActions = doUpdate(List.of(Mock.of(1L, "mock")));
        checkResultActionsAccessDined(resultActions);
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
        String oldName = mockMapper.selectById(1L).getName();

        ResultActions resultActions = doUpdate(List.of(Mock.of(1L, null)));
        checkResultActionsSuccess(resultActions);

        Assertions.assertEquals(oldName, mockMapper.selectById(1L).getName());
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
    @Sql(scripts = {"/sql/test/ddl/mock.sql"})
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
}
