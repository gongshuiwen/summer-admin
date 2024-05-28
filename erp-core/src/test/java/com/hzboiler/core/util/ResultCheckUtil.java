package com.hzboiler.core.util;

import com.hzboiler.core.protocal.R;
import org.hamcrest.core.Is;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static com.hzboiler.core.exception.CoreBusinessExceptionEnums.*;

public class ResultCheckUtil {

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
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", Is.is(ERROR_ACCESS_DENIED.code())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Is.is(ERROR_ACCESS_DENIED.message())));
    }

    public static void checkResultActionsAuthenticationFailed(ResultActions resultActions) throws Exception {
        resultActions.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", Is.is(ERROR_AUTHENTICATION_FAILED.code())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Is.is(ERROR_AUTHENTICATION_FAILED.message())));
    }

    public static void checkResultActionsInvalidArguments(ResultActions resultActions) throws Exception {
        resultActions.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", Is.is(ERROR_INVALID_ARGUMENTS.code())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Is.is(ERROR_INVALID_ARGUMENTS.message())));
    }
}
