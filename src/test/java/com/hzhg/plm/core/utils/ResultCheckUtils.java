package com.hzhg.plm.core.utils;

import com.hzhg.plm.core.exception.BusinessExceptionEnum;
import com.hzhg.plm.core.protocal.R;
import org.hamcrest.core.Is;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

public class ResultCheckUtils {

    public static void checkResultActionsSuccess(ResultActions resultActions) throws Exception {
        resultActions.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", Is.is(R.SUCCESS_CODE)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Is.is(R.SUCCESS_MESSAGE)));
    }

    public static void checkResultActionsAccessDined(ResultActions resultActions) throws Exception {
        resultActions.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", Is.is(BusinessExceptionEnum.ERROR_ACCESS_DENIED.getCode())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Is.is(BusinessExceptionEnum.ERROR_ACCESS_DENIED.getMessage())));
    }

    public static void checkResultActionsAuthenticationFailed(ResultActions resultActions) throws Exception {
        resultActions.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", Is.is(BusinessExceptionEnum.ERROR_AUTHENTICATION_FAILED.getCode())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Is.is(BusinessExceptionEnum.ERROR_AUTHENTICATION_FAILED.getMessage())));
    }

    public static void checkResultActionsInvalidArguments(ResultActions resultActions) throws Exception {
        resultActions.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", Is.is(BusinessExceptionEnum.ERROR_INVALID_ARGUMENTS.getCode())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Is.is(BusinessExceptionEnum.ERROR_INVALID_ARGUMENTS.getMessage())));
    }
}
