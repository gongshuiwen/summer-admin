package com.hzboiler.erp.core.controller;

import com.hzboiler.erp.core.annotaion.WithMockAdmin;
import com.hzboiler.erp.core.model.Mock;
import com.hzboiler.erp.core.protocal.query.Condition;
import com.hzboiler.erp.core.protocal.query.SimpleCondition;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

/**
 * @author gongshuiwen
 */
@Sql(scripts = {"/sql/test/ddl/mock.sql", "/sql/test/data/mock.sql"})
class TestCommonControllerPage extends CommonControllerTestBase {

    ResultActions doPage(Long pageNum, Long pageSize, String orderBys, Condition condition) throws Exception {
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get(MOCK_PATH + "/page");
        builder.contentType(MediaType.APPLICATION_JSON);
        if (pageNum != null) {
            builder.param("pageNum", pageNum.toString());
        }
        if (pageSize != null) {
            builder.param("pageSize", pageSize.toString());
        }
        if (orderBys != null) {
            builder.param("orderBys", orderBys);
        }
        if (condition != null) {
            builder.content(objectMapper.writeValueAsBytes(condition));
        }
        return mockMvc.perform(builder);
    }

    @Override
    ResultActions doExample() throws Exception {
        return doPage(1L, 20L, null, null);
    }

    @Test
    @WithMockUser(authorities = MOCK_AUTHORITY_SELECT)
    void testAuthorized() throws Exception {
        List<Long> ids = List.of(1L, 2L);
        List<Mock> mocks = mockService.getBaseMapper().selectBatchIds(ids);

        ResultActions resultActions = doPage(1L, 20L, null, null);

        checkResultActionsSuccess(resultActions);
        resultActions
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.current", Is.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.size", Is.is(20)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.total", Is.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pages", Is.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.records[0].id", Is.is(mocks.get(0).getId().toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.records[0].name", Is.is(mocks.get(0).getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.records[1].id", Is.is(mocks.get(1).getId().toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.records[1].name", Is.is(mocks.get(1).getName())))
        ;
    }

    @Test
    @WithMockAdmin
    void testPageNumNotValid() throws Exception {
        checkResultActionsInvalidArguments(doPage(null, 20L, null, null));
        checkResultActionsInvalidArguments(doPage(0L, 20L, null, null));
        checkResultActionsInvalidArguments(doPage(-1L, 20L, null, null));
        checkResultActionsInvalidArguments(doPage(1001L, 20L, null, null));
    }

    @Test
    @WithMockAdmin
    void testPageSizeNotValid() throws Exception {
        checkResultActionsInvalidArguments(doPage(1L, null, null, null));
        checkResultActionsInvalidArguments(doPage(1L, 0L, null, null));
        checkResultActionsInvalidArguments(doPage(1L, -1L, null, null));
        checkResultActionsInvalidArguments(doPage(1L, 1001L, null, null));
    }

    @Test
    @WithMockAdmin
    void testOrderByIdDesc() throws Exception {
        List<Long> ids = List.of(1L, 2L);
        List<Mock> mocks = mockService.getBaseMapper().selectBatchIds(ids);

        ResultActions resultActions = doPage(1L, 20L, "_id", null);

        checkResultActionsSuccess(resultActions);
        resultActions
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.current", Is.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.size", Is.is(20)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.total", Is.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pages", Is.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.records[0].id", Is.is(mocks.get(1).getId().toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.records[0].name", Is.is(mocks.get(1).getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.records[1].id", Is.is(mocks.get(0).getId().toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.records[1].name", Is.is(mocks.get(0).getName())))
        ;
    }

    @Test
    @WithMockAdmin
    void testSimpleCondition() throws Exception {
        Condition condition = SimpleCondition.of("name", "=", "mock1");
        ResultActions resultActions = doPage(1L, 20L, null, condition);
        checkResultActionsSuccess(resultActions);
        resultActions
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.current", Is.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.size", Is.is(20)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.total", Is.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pages", Is.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.records[0].id", Is.is("1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.records[0].name", Is.is("mock1")))
        ;
    }
}
