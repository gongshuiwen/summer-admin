package io.summernova.admin.spring.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.summernova.admin.common.exception.BusinessExceptionEnum;
import org.hamcrest.core.Is;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static io.summernova.admin.common.exception.CoreBusinessExceptionEnums.ERROR_INVALID_ARGUMENTS;

/**
 * @author gongshuiwen
 */
@SpringBootTest
@AutoConfigureMockMvc
public abstract class ControllerTestBase {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    @Qualifier("mappingJackson2HttpMessageConverterObjectMapper")
    protected ObjectMapper objectMapper;

    protected void checkResultActionsSuccess(ResultActions resultActions) throws Exception {
        resultActions.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").doesNotExist());
    }

    protected void checkResultActionsSuccess(ResultActions resultActions, Object value) throws Exception {
        resultActions.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data", Is.is(value)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").doesNotExist());
    }

    protected void checkResultActionsException(ResultActions resultActions, BusinessExceptionEnum businessExceptionEnum) throws Exception {
        resultActions.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").doesNotExist())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error.namespace", Is.is(businessExceptionEnum.namespace())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error.code", Is.is(businessExceptionEnum.code())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error.message", Is.is(businessExceptionEnum.message())));
    }

    protected void checkResultActionsInvalidArguments(ResultActions resultActions) throws Exception {
        checkResultActionsException(resultActions, ERROR_INVALID_ARGUMENTS);
    }
}
