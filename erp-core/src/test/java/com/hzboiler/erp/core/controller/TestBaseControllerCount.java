package com.hzboiler.erp.core.controller;

import com.hzboiler.erp.core.annotaion.WithMockAdmin;
import com.hzboiler.erp.core.model.Mock;
import com.hzboiler.erp.core.protocal.Condition;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static com.hzboiler.erp.core.exception.CoreBusinessExceptionEnums.*;

/**
 * @author gongshuiwen
 */
@Sql(scripts = {"/sql/test/ddl/mock.sql", "/sql/test/data/mock.sql"})
class TestBaseControllerCount extends MockControllerTestBase {

    ResultActions doCount(Condition<?> condition) throws Exception {
        return mockMvc.perform(
                MockMvcRequestBuilders
                        .get(MOCK_PATH + "/count")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(condition == null ? new byte[0] : objectMapper.writeValueAsBytes(condition)))
                ;
    }

    @Test
    @WithAnonymousUser
    void testAnonymous() throws Exception {
        checkResultActionsException(doCount(null), ERROR_AUTHENTICATION_FAILED);
    }

    @Test
    @WithMockUser
    void testNotAuthorized() throws Exception {
        checkResultActionsException(doCount(null), ERROR_ACCESS_DENIED);
    }

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql", "/sql/test/data/mock.sql"})
    @WithMockUser(authorities = MOCK_AUTHORITY_SELECT)
    void testAuthorized() throws Exception {
        ResultActions resultActions = doCount(null);
        checkResultActionsSuccess(resultActions);
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.data", Is.is("2")));
    }

    @Test
    @WithMockAdmin
    void testAdmin() throws Exception {
        testAuthorized();
    }
}
