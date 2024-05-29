package com.hzboiler.erp.core.controller;

import com.hzboiler.erp.core.annotaion.WithMockAdmin;
import com.hzboiler.erp.core.model.Mock;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author gongshuiwen
 */
@Sql(scripts = {"/sql/test/ddl/mock.sql", "/sql/test/data/mock.sql"})
class TestBaseControllerSelect extends MockControllerTestBase {

    ResultActions doSelect(List<Long> ids) throws Exception {
        return mockMvc.perform(
                MockMvcRequestBuilders
                        .get(MOCK_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("ids", ids.stream().map(String::valueOf).collect(Collectors.joining(","))));
    }

    @Override
    ResultActions doExample() throws Exception {
        return doSelect(List.of(1L, 2L));
    }

    @Test
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
    void testIdsEmpty() throws Exception {
        checkResultActionsInvalidArguments(doSelect(List.of()));
    }
}
