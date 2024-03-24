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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.hzhg.plm.core.controller.BaseController.*;
import static com.hzhg.plm.core.utils.ResultCheckUtils.*;


@SpringBootTest
@AutoConfigureMockMvc
public class TestBaseControllerUpdateByIds {

    static final String MOCK_PATH = "/mock";
    static final String MOCK_PATH_BATCH = MOCK_PATH + "/batch";
    static final String MOCK_ENTITY_NAME = "Mock";
    static final String MOCK_AUTHORITY_UPDATE = MOCK_ENTITY_NAME + AUTHORITY_DELIMITER + AUTHORITY_UPDATE;

    MockMvc mockMvc;

    MockMapper mockMapper;

    ObjectMapper objectMapper;

    public TestBaseControllerUpdateByIds(
            @Autowired MockMvc mockMvc,
            @Autowired MockMapper mockMapper,
            @Autowired MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter) {
        this.mockMvc = mockMvc;
        this.mockMapper = mockMapper;
        this.objectMapper = mappingJackson2HttpMessageConverter.getObjectMapper();
    }

    ResultActions doUpdateByIds(List<Long> updateIds, Mock mock) throws Exception {
        return mockMvc.perform(
                MockMvcRequestBuilders
                        .put(MOCK_PATH_BATCH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new BatchUpdateDto<>(updateIds, mock))));
    }

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql", "/sql/test/data/mock.sql"})
    @WithMockUser(authorities = MOCK_AUTHORITY_UPDATE)
    void testUpdateByIdsAuthorized() throws Exception {
        long count = mockMapper.selectCount(null);
        String updateName = "mock";
        List<Long> updateIds = Arrays.asList(1L, 2L);
        updateIds.forEach(id -> Assertions.assertNotEquals(updateName, mockMapper.selectById(id).getName()));

        ResultActions resultActions = doUpdateByIds(updateIds, new Mock(updateName));

        checkResultActionsSuccess(resultActions);
        Assertions.assertEquals(count, mockMapper.selectCount(null));
        for (Long updateId : updateIds) {
            Mock mock = mockMapper.selectById(updateId);
            Assertions.assertEquals(updateName, mock.getName());
            Assertions.assertEquals(0, mock.getCreateUser());
            Assertions.assertEquals(0, mock.getUpdateUser());
        }
    }

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql", "/sql/test/data/mock.sql"})
    @WithMockUser
    void testUpdateByIdsNotAuthorized() throws Exception {
        ResultActions resultActions = doUpdateByIds(Arrays.asList(1L, 2L), new Mock("mock"));
        checkResultActionsAccessDined(resultActions);
    }

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql", "/sql/test/data/mock.sql"})
    @WithMockAdmin
    void testUpdateByIdsAdmin() throws Exception {
        testUpdateByIdsAuthorized();
    }

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql", "/sql/test/data/mock.sql"})
    @WithAnonymousUser
    void testUpdateByIdsAnonymous() throws Exception {
        ResultActions resultActions = doUpdateByIds(Arrays.asList(1L, 2L), new Mock("mock"));
        checkResultActionsAuthenticationFailed(resultActions);
    }

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql", "/sql/test/data/mock.sql"})
    @WithMockAdmin
    void testUpdateByIdsInvalidArguments1() throws Exception {
        ResultActions resultActions = doUpdateByIds(null, new Mock(""));
        checkResultActionsInvalidArguments(resultActions);
    }

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql", "/sql/test/data/mock.sql"})
    @WithMockAdmin
    void testUpdateByIdsInvalidArguments2() throws Exception {
        ResultActions resultActions = doUpdateByIds(new ArrayList<>(), new Mock(""));
        checkResultActionsInvalidArguments(resultActions);
    }

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql", "/sql/test/data/mock.sql"})
    @WithMockAdmin
    void testUpdateByIdsInvalidArguments3() throws Exception {
        ResultActions resultActions = doUpdateByIds(Arrays.asList(1L, 2L), null);
        checkResultActionsInvalidArguments(resultActions);
    }
}
