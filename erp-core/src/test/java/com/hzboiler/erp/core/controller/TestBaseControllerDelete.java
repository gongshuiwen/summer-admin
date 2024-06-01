package com.hzboiler.erp.core.controller;

import com.hzboiler.erp.core.annotaion.WithMockAdmin;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author gongshuiwen
 */
@Sql(scripts = {"/sql/test/ddl/mock.sql", "/sql/test/data/mock.sql"})
class TestBaseControllerDelete extends MockControllerTestBase {

    ResultActions doDelete(List<Long> ids) throws Exception {
        return mockMvc.perform(
                MockMvcRequestBuilders
                        .delete(MOCK_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("ids", ids.stream().map(String::valueOf).collect(Collectors.joining(","))));
    }

    @Override
    ResultActions doExample() throws Exception {
        return doDelete(Arrays.asList(1L, 2L));
    }

    @Test
    @WithMockUser(authorities = MOCK_AUTHORITY_DELETE)
    void testAuthorized() throws Exception {
        List<Long> deleteIds = Arrays.asList(1L, 2L);
        deleteIds.forEach(id -> Assertions.assertNotNull(mockMapper.selectById(id)));

        ResultActions resultActions = doDelete(deleteIds);

        checkResultActionsSuccess(resultActions, true);
        deleteIds.forEach(deleteId -> Assertions.assertNull(mockMapper.selectById(deleteId)));
    }

    @Test
    @WithMockAdmin
    void testIdsEmpty() throws Exception {
        checkResultActionsInvalidArguments(doDelete(List.of()));
    }
}
