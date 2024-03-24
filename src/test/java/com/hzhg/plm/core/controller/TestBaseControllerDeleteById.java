package com.hzhg.plm.core.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hzhg.plm.core.annotaion.WithMockAdmin;
import com.hzhg.plm.core.mapper.MockMapper;
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

import static com.hzhg.plm.core.controller.BaseController.AUTHORITY_DELETE;
import static com.hzhg.plm.core.controller.BaseController.AUTHORITY_DELIMITER;
import static com.hzhg.plm.core.utils.ResultCheckUtils.*;


@SpringBootTest
@AutoConfigureMockMvc
public class TestBaseControllerDeleteById {

    static final String MOCK_PATH = "/mock";
    static final String MOCK_ENTITY_NAME = "Mock";
    static final String MOCK_AUTHORITY_DELETE = MOCK_ENTITY_NAME + AUTHORITY_DELIMITER + AUTHORITY_DELETE;

    MockMvc mockMvc;

    MockMapper mockMapper;

    ObjectMapper objectMapper;

    public TestBaseControllerDeleteById(
            @Autowired MockMvc mockMvc,
            @Autowired MockMapper mockMapper,
            @Autowired MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter) {
        this.mockMvc = mockMvc;
        this.mockMapper = mockMapper;
        this.objectMapper = mappingJackson2HttpMessageConverter.getObjectMapper();
    }

    ResultActions doDeleteById(long deleteId) throws Exception {
        return mockMvc.perform(
                MockMvcRequestBuilders
                        .delete(MOCK_PATH + "/" + deleteId)
                        .contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql", "/sql/test/data/mock.sql"})
    @WithMockUser(authorities = MOCK_AUTHORITY_DELETE)
    void testDeleteByIdAuthorized() throws Exception {
        long deleteId = 1;
        long count = mockMapper.selectCount(null);
        Assertions.assertNotNull(mockMapper.selectById(deleteId));

        ResultActions resultActions = doDeleteById(deleteId);

        checkResultActionsSuccess(resultActions);
        Assertions.assertEquals(count - 1, mockMapper.selectCount(null));
        Assertions.assertNull(mockMapper.selectById(deleteId));
    }

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql", "/sql/test/data/mock.sql"})
    @WithMockUser
    void testDeleteByIdNotAuthorized() throws Exception {
        ResultActions resultActions = doDeleteById(1);
        checkResultActionsAccessDined(resultActions);
    }

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql", "/sql/test/data/mock.sql"})
    @WithMockAdmin
    void testDeleteByIdAdmin() throws Exception {
        testDeleteByIdAuthorized();
    }

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql", "/sql/test/data/mock.sql"})
    @WithAnonymousUser
    void testDeleteByIdAnonymous() throws Exception {
        ResultActions resultActions = doDeleteById(1);
        checkResultActionsAuthenticationFailed(resultActions);
    }
}
