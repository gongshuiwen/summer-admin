package com.hzboiler.core.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hzboiler.core.mapper.MockMapper;
import com.hzboiler.core.annotaion.WithMockAdmin;
import com.hzboiler.core.entity.Mock;
import org.hamcrest.core.Is;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.hzboiler.core.security.DataAccessAuthority.AUTHORITY_CREATE;
import static com.hzboiler.core.utils.ResultCheckUtil.*;


@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = {"/sql/test/ddl/mock.sql"})
public class TestBaseControllerCreate {

    static final String MOCK_PATH = "/mock";
    static final String MOCK_ENTITY_NAME = "Mock";
    static final String MOCK_AUTHORITY_CREATE = MOCK_ENTITY_NAME + ":" + AUTHORITY_CREATE;
    static final List<Mock> MOCKS = List.of(new Mock("mock1"), new Mock("mock2"));

    MockMvc mockMvc;

    MockMapper mockMapper;

    ObjectMapper objectMapper;

    public TestBaseControllerCreate(
            @Autowired MockMvc mockMvc,
            @Autowired MockMapper mockMapper,
            @Autowired MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter) {
        this.mockMvc = mockMvc;
        this.mockMapper = mockMapper;
        this.objectMapper = mappingJackson2HttpMessageConverter.getObjectMapper();
    }

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
        ResultActions resultActions = doCreate(MOCKS);
        checkResultActionsAuthenticationFailed(resultActions);
    }

    @Test
    @WithMockUser
    void testNotAuthorized() throws Exception {
        ResultActions resultActions = doCreate(MOCKS);
        checkResultActionsAccessDined(resultActions);
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
}
