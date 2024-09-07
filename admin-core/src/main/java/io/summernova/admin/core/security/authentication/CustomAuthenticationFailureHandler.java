package io.summernova.admin.core.security.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.summernova.admin.core.protocal.Result;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static io.summernova.admin.core.exception.CoreBusinessExceptionEnums.ERROR_AUTHENTICATION_FAILED;

/**
 * @author gongshuiwen
 */
public record CustomAuthenticationFailureHandler(
        ObjectMapper objectMapper) implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(objectMapper.writeValueAsString(Result.error(ERROR_AUTHENTICATION_FAILED)));
    }
}