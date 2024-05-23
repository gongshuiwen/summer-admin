package com.hzboiler.core.controller;

import com.hzboiler.core.mapper.MockMapper;
import com.hzboiler.core.annotaion.WithMockAdmin;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.hzboiler.core.exception.CoreBusinessExceptionEnums.*;
import static com.hzboiler.core.security.DataAccessAuthority.AUTHORITY_DELETE;

@Sql(scripts = {"/sql/test/ddl/mock.sql", "/sql/test/data/mock.sql"})
public class TestBaseControllerDelete extends ControllerTestBase {

    static final String MOCK_PATH = "/mock";
    static final String MOCK_ENTITY_NAME = "Mock";
    static final String MOCK_AUTHORITY_DELETE = MOCK_ENTITY_NAME + ":" + AUTHORITY_DELETE;

    @Autowired
    MockMapper mockMapper;

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
        checkResultActionsException(doDelete(Arrays.asList(1L, 2L)), ERROR_AUTHENTICATION_FAILED);
    }

    @Test
    @WithMockUser
    void testNotAuthorized() throws Exception {
        checkResultActionsException(doDelete(Arrays.asList(1L, 2L)), ERROR_ACCESS_DENIED);
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
        checkResultActionsException(doDelete(List.of()), ERROR_INVALID_ARGUMENTS);
    }
}
