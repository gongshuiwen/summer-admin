package com.hzhg.plm.core.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hzhg.plm.core.annotaion.WithMockAdmin;
import com.hzhg.plm.core.entity.Mock;
import com.hzhg.plm.core.mapper.MockMapper;
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

import static com.hzhg.plm.core.controller.BaseController.AUTHORITY_CREATE;
import static com.hzhg.plm.core.controller.BaseController.AUTHORITY_DELIMITER;
import static com.hzhg.plm.core.utils.ResultCheckUtils.*;


@SpringBootTest
@AutoConfigureMockMvc
public class TestBaseControllerCreateOne {

    static final String MOCK_PATH = "/mock";
    static final String MOCK_ENTITY_NAME = "Mock";
    static final String MOCK_AUTHORITY_CREATE = MOCK_ENTITY_NAME + AUTHORITY_DELIMITER + AUTHORITY_CREATE;

    MockMvc mockMvc;

    MockMapper mockMapper;

    ObjectMapper objectMapper;

    public TestBaseControllerCreateOne(
            @Autowired MockMvc mockMvc,
            @Autowired MockMapper mockMapper,
            @Autowired MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter) {
        this.mockMvc = mockMvc;
        this.mockMapper = mockMapper;
        this.objectMapper = mappingJackson2HttpMessageConverter.getObjectMapper();
    }

    ResultActions doCreateOne(String name) throws Exception {
        return mockMvc.perform(
                MockMvcRequestBuilders
                        .post(MOCK_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new Mock(name))));
    }

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql"})
    @WithMockUser(authorities = MOCK_AUTHORITY_CREATE)
    void testCreateOneAuthorized() throws Exception {
        String name = "mock";
        long returnId = 1;
        Assertions.assertEquals(0, mockMapper.selectCount(null));

        ResultActions resultActions = doCreateOne(name);

        checkResultActionsSuccess(resultActions);
        resultActions
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id", Is.is(Long.toString(returnId))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name", Is.is(name)))
        ;

        Assertions.assertEquals(1, mockMapper.selectCount(null));

        Mock mock = mockMapper.selectById(returnId);
        Assertions.assertEquals(name, mock.getName());
        Assertions.assertEquals(0, mock.getCreateUser());
        Assertions.assertEquals(0, mock.getUpdateUser());
    }

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql"})
    @WithMockUser
    void testCreateOneNotAuthorized() throws Exception {
        ResultActions resultActions = doCreateOne("mock");
        checkResultActionsAccessDined(resultActions);
    }

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql"})
    @WithMockAdmin
    void testCreateOneAdmin() throws Exception {
        testCreateOneAuthorized();
    }

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql"})
    @WithAnonymousUser
    void testCreateOneAnonymous() throws Exception {
        ResultActions resultActions = doCreateOne("mock");
        checkResultActionsAuthenticationFailed(resultActions);
    }
}
