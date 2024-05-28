package com.hzboiler.erp.core.security.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hzboiler.erp.core.context.BaseContextHolder;
import com.hzboiler.erp.core.protocal.R;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static com.hzboiler.erp.core.exception.CoreBusinessExceptionEnums.ERROR_AUTHENTICATION_FAILED;

/**
 * @author gongshuiwen
 */
public record CustomAuthenticationEntryPoint(ObjectMapper objectMapper) implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        // clear BaseContext for anonymous user
        BaseContextHolder.clearContext();
        response.getWriter().write(objectMapper.writeValueAsString(R.error(ERROR_AUTHENTICATION_FAILED)));
    }
}