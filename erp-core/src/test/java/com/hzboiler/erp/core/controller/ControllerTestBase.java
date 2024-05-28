package com.hzboiler.erp.core.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hzboiler.erp.core.exception.BusinessExceptionEnum;
import com.hzboiler.erp.core.protocal.R;
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
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", Is.is(R.SUCCESS_CODE)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Is.is(R.SUCCESS_MESSAGE)));
    }

    protected void checkResultActionsException(ResultActions resultActions, BusinessExceptionEnum businessExceptionEnum) throws Exception {
        resultActions.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", Is.is(businessExceptionEnum.code())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Is.is(businessExceptionEnum.message())));
    }
}
