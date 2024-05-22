package com.hzboiler.core.security.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hzboiler.core.protocal.R;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static com.hzboiler.core.exception.CoreBusinessExceptionEnums.ERROR_AUTHENTICATION_FAILED;

public record CustomAuthenticationFailureHandler(
        ObjectMapper objectMapper) implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(objectMapper.writeValueAsString(R.error(ERROR_AUTHENTICATION_FAILED)));
    }
}