package com.hzboiler.erp.core.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hzboiler.erp.core.exception.BusinessExceptionEnum;
import com.hzboiler.erp.core.protocal.Result;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static com.hzboiler.erp.core.exception.CoreBusinessExceptionEnums.ERROR_INVALID_ARGUMENTS;
import static org.hamcrest.Matchers.nullValue;

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
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        // check the error field is null
        Assertions.assertThrowsExactly(AssertionError.class, () ->
                resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.error", nullValue())));
        Result<?> result = objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), Result.class);
        Assertions.assertNull(result.getError());
    }

    protected void checkResultActionsSuccess(ResultActions resultActions, Object value) throws Exception {
        resultActions.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data", Is.is(value)));

        // check the error field is null
        Assertions.assertThrowsExactly(AssertionError.class, () ->
                resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.error", nullValue())));
        Result<?> result = objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), Result.class);
        Assertions.assertNull(result.getError());
    }

    protected void checkResultActionsException(ResultActions resultActions, BusinessExceptionEnum businessExceptionEnum) throws Exception {
        resultActions.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error.namespace", Is.is(businessExceptionEnum.namespace())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error.code", Is.is(businessExceptionEnum.code())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error.message", Is.is(businessExceptionEnum.message())));
    }

    protected void checkResultActionsInvalidArguments(ResultActions resultActions) throws Exception {
        checkResultActionsException(resultActions, ERROR_INVALID_ARGUMENTS);
    }
}
