package io.summernova.admin.spring.security.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.summernova.admin.common.protocal.Result;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author gongshuiwen
 */
public record CustomAuthenticationSuccessHandler(
        ObjectMapper objectMapper) implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        response.getWriter().write(objectMapper.writeValueAsString(Result.success(userDetails)));
    }
}