package com.hzboiler.core.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hzboiler.core.mapper.MockMapper;
import com.hzboiler.core.annotaion.WithMockAdmin;
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

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.hzboiler.core.security.DataAccessAuthority.AUTHORITY_DELETE;
import static com.hzboiler.core.utils.ResultCheckUtil.*;


@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = {"/sql/test/ddl/mock.sql", "/sql/test/data/mock.sql"})
public class TestBaseControllerDelete {

    static final String MOCK_PATH = "/mock";
    static final String MOCK_ENTITY_NAME = "Mock";
    static final String MOCK_AUTHORITY_DELETE = MOCK_ENTITY_NAME + ":" + AUTHORITY_DELETE;

    MockMvc mockMvc;

    MockMapper mockMapper;

    ObjectMapper objectMapper;

    public TestBaseControllerDelete(
            @Autowired MockMvc mockMvc,
            @Autowired MockMapper mockMapper,
            @Autowired MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter) {
        this.mockMvc = mockMvc;
        this.mockMapper = mockMapper;
        this.objectMapper = mappingJackson2HttpMessageConverter.getObjectMapper();
    }

    ResultActions doDelete(List<Long> ids) throws Exception {
        return mockMvc.perform(
                MockMvcRequestBuilders
                        .delete(MOCK_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("ids", ids.stream().map(String::valueOf).collect(Collectors.joining(","))));
    }

    @Test
    @WithAnonymousUser
    void testAnonymous() throws Exception {
        ResultActions resultActions = doDelete(Arrays.asList(1L, 2L));
        checkResultActionsAuthenticationFailed(resultActions);
    }

    @Test
    @WithMockUser
    void testNotAuthorized() throws Exception {
        ResultActions resultActions = doDelete(Arrays.asList(1L, 2L));
        checkResultActionsAccessDined(resultActions);
    }

    @Test
    @WithMockUser(authorities = MOCK_AUTHORITY_DELETE)
    void testAuthorized() throws Exception {
        List<Long> deleteIds = Arrays.asList(1L, 2L);
        deleteIds.forEach(id -> Assertions.assertNotNull(mockMapper.selectById(id)));

        ResultActions resultActions = doDelete(deleteIds);

        checkResultActionsSuccess(resultActions);
        deleteIds.forEach(deleteId -> Assertions.assertNull(mockMapper.selectById(deleteId)));
    }

    @Test
    @WithMockAdmin
    void testAdmin() throws Exception {
        testAuthorized();
    }

    @Test
    @WithMockAdmin
    void testIdsEmpty() throws Exception {
        checkResultActionsInvalidArguments(doDelete(List.of()));
    }
}
