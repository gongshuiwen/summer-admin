package com.hzboiler.erp.core.controller;

import com.hzboiler.erp.core.annotaion.WithMockAdmin;
import com.hzboiler.erp.core.protocal.query.Condition;
import com.hzboiler.erp.core.protocal.query.SimpleCondition;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

/**
 * @author gongshuiwen
 */
@Sql(scripts = {"/sql/test/ddl/mock.sql", "/sql/test/data/mock.sql"})
class TestBaseControllerCount extends MockControllerTestBase {

    ResultActions doCount(Condition condition) throws Exception {
        return mockMvc.perform(
                MockMvcRequestBuilders
                        .get(MOCK_PATH + "/count")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(condition == null ? new byte[0] : objectMapper.writeValueAsBytes(condition)))
                ;
    }

    @Override
    ResultActions doExample() throws Exception {
        return doCount(null);
    }

    @Test
    @WithMockUser(authorities = MOCK_AUTHORITY_SELECT)
    void testAuthorized() throws Exception {
        ResultActions resultActions = doCount(null);
        checkResultActionsSuccess(resultActions, "2");
    }

    @Test
    @WithMockAdmin
    void testCondition() throws Exception {
        Condition condition = SimpleCondition.of("name", "=", "mock1");
        ResultActions resultActions = doCount(condition);
        checkResultActionsSuccess(resultActions, "1");
    }
}
