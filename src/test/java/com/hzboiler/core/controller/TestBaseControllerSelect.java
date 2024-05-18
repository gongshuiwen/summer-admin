package com.hzboiler.core.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hzboiler.core.annotaion.WithMockAdmin;
import com.hzboiler.core.entity.Mock;
import com.hzboiler.core.mapper.MockMapper;
import org.hamcrest.core.Is;
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

import java.util.List;
import java.util.stream.Collectors;

import static com.hzboiler.core.security.DataAccessAuthority.AUTHORITY_SELECT;
import static com.hzboiler.core.utils.ResultCheckUtil.*;


@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = {"/sql/test/ddl/mock.sql", "/sql/test/data/mock.sql"})
public class TestBaseControllerSelect {

    static final String MOCK_PATH = "/mock";
    static final String MOCK_ENTITY_NAME = "Mock";
    static final String MOCK_AUTHORITY_SELECT = MOCK_ENTITY_NAME + ":" + AUTHORITY_SELECT;

    MockMvc mockMvc;

    MockMapper mockMapper;

    ObjectMapper objectMapper;

    public TestBaseControllerSelect(
            @Autowired MockMvc mockMvc,
            @Autowired MockMapper mockMapper,
            @Autowired MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter) {
        this.mockMvc = mockMvc;
        this.mockMapper = mockMapper;
        this.objectMapper = mappingJackson2HttpMessageConverter.getObjectMapper();
    }

    ResultActions doSelect(List<Long> ids) throws Exception {
        return mockMvc.perform(
                MockMvcRequestBuilders
                        .get(MOCK_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("ids", ids.stream().map(String::valueOf).collect(Collectors.joining(","))));
    }

    @Test
    @WithAnonymousUser
    void testAnonymous() throws Exception {
        ResultActions resultActions = doSelect(List.of(1L, 2L));
        checkResultActionsAuthenticationFailed(resultActions);
    }

    @Test
    @WithMockUser
    void testNotAuthorized() throws Exception {
        ResultActions resultActions = doSelect(List.of(1L, 2L));
        checkResultActionsAccessDined(resultActions);
    }

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql", "/sql/test/data/mock.sql"})
    @WithMockUser(authorities = MOCK_AUTHORITY_SELECT)
    void testAuthorized() throws Exception {
        List<Long> ids = List.of(1L, 2L);
        List<Mock> mocks = mockMapper.selectBatchIds(ids);

        ResultActions resultActions = doSelect(ids);

        checkResultActionsSuccess(resultActions);
        resultActions
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id", Is.is(mocks.get(0).getId().toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].name", Is.is(mocks.get(0).getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].id", Is.is(mocks.get(1).getId().toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].name", Is.is(mocks.get(1).getName())))
        ;
    }

    @Test
    @WithMockAdmin
    void testAdmin() throws Exception {
        testAuthorized();
    }

    @Test
    @WithMockAdmin
    void testIdsEmpty() throws Exception {
        checkResultActionsInvalidArguments(doSelect(List.of()));
    }
}
