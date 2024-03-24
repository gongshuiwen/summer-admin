package com.hzhg.plm.core.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hzhg.plm.core.annotaion.WithMockAdmin;
import com.hzhg.plm.core.entity.Mock;
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

import static com.hzhg.plm.core.controller.BaseController.AUTHORITY_DELIMITER;
import static com.hzhg.plm.core.controller.BaseController.AUTHORITY_UPDATE;
import static com.hzhg.plm.core.utils.ResultCheckUtils.*;


@SpringBootTest
@AutoConfigureMockMvc
public class TestBaseControllerUpdateById {

    static final String MOCK_PATH = "/mock";
    static final String MOCK_ENTITY_NAME = "Mock";
    static final String MOCK_AUTHORITY_UPDATE = MOCK_ENTITY_NAME + AUTHORITY_DELIMITER + AUTHORITY_UPDATE;

    MockMvc mockMvc;

    MockMapper mockMapper;

    ObjectMapper objectMapper;

    public TestBaseControllerUpdateById(
            @Autowired MockMvc mockMvc,
            @Autowired MockMapper mockMapper,
            @Autowired MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter) {
        this.mockMvc = mockMvc;
        this.mockMapper = mockMapper;
        this.objectMapper = mappingJackson2HttpMessageConverter.getObjectMapper();
    }

    ResultActions doUpdateById(long updateId, String updateName) throws Exception {
        return mockMvc.perform(
                MockMvcRequestBuilders
                        .put(MOCK_PATH + "/" + updateId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new Mock(updateName))));
    }

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql", "/sql/test/data/mock.sql"})
    @WithMockUser(authorities = MOCK_AUTHORITY_UPDATE)
    void testUpdateByIdAuthorized() throws Exception {
        long updateId = 1;
        String updateName = "mock";
        long count = mockMapper.selectCount(null);
        Assertions.assertNotEquals(updateName, mockMapper.selectById(updateId).getName());

        ResultActions resultActions = doUpdateById(updateId, updateName);

        checkResultActionsSuccess(resultActions);
        Assertions.assertEquals(count, mockMapper.selectCount(null));

        Mock mock = mockMapper.selectById(updateId);
        Assertions.assertEquals(updateName, mock.getName());
        Assertions.assertEquals(0, mock.getCreateUser());
        Assertions.assertEquals(0, mock.getUpdateUser());
    }

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql", "/sql/test/data/mock.sql"})
    @WithMockUser
    void testUpdateByIdNotAuthorized() throws Exception {
        ResultActions resultActions = doUpdateById(1, "mock");
        checkResultActionsAccessDined(resultActions);
    }

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql", "/sql/test/data/mock.sql"})
    @WithMockAdmin
    void testUpdateByIdAdmin() throws Exception {
        testUpdateByIdAuthorized();
    }

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql", "/sql/test/data/mock.sql"})
    @WithAnonymousUser
    void testUpdateByIdAnonymous() throws Exception {
        ResultActions resultActions = doUpdateById(1, "mock");
        checkResultActionsAuthenticationFailed(resultActions);
    }
}
