package com.hzboiler.core.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class CustomUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final ObjectMapper objectMapper;

    public CustomUsernamePasswordAuthenticationFilter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (request.getContentType().startsWith(MediaType.APPLICATION_JSON_VALUE)) {
            LoginDto loginDto;
            try {
                loginDto = objectMapper.readValue(request.getInputStream(), LoginDto.class);
            } catch ( IOException e ) {
                throw new AuthenticationServiceException("Cannot parse login info!");
            }

            UsernamePasswordAuthenticationToken authRequest =
                    UsernamePasswordAuthenticationToken.unauthenticated(loginDto.getUsername(), loginDto.getPassword());
            authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
            return this.getAuthenticationManager().authenticate(authRequest);
        }
        throw new AuthenticationServiceException("Authentication request's content type is invalid!" );
    }

    @Getter
    @Setter
    private static class LoginDto {
        private String username;
        private String password;
    }
}
