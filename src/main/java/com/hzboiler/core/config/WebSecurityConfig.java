package com.hzboiler.core.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hzboiler.core.exception.BusinessExceptionEnum;
import com.hzboiler.core.protocal.R;
import com.hzboiler.core.security.CustomUsernamePasswordAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Configuration
@EnableMethodSecurity
public class WebSecurityConfig {

    private final ObjectMapper objectMapper;

    public WebSecurityConfig(MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter) {
        this.objectMapper = mappingJackson2HttpMessageConverter.getObjectMapper();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            DaoAuthenticationProvider daoAuthenticationProvider,
            UsernamePasswordAuthenticationFilter filter
        ) throws Exception {

        http
                // Disable CSRF protection for separation architecture
                .csrf(AbstractHttpConfigurer::disable)

                // Config login filter
                .formLogin(AbstractHttpConfigurer::disable)
                .addFilterAt(filter, UsernamePasswordAuthenticationFilter.class)

                // Config logout filter
                .logout(configurer -> configurer
                        .logoutSuccessHandler(new CustomLogoutSuccessHandler(objectMapper)))

                // Config exception handle filter
                .exceptionHandling(configurer -> configurer
                        .authenticationEntryPoint(new CustomAuthenticationEntryPoint(objectMapper))
                        .accessDeniedHandler(new CustomAccessDeniedHandler(objectMapper)))

                // Static configurations for requests authorization
                .authorizeHttpRequests(configurer -> configurer
                        .requestMatchers("/doc.html").permitAll()
                        .requestMatchers("/webjars/**").permitAll()
                        .requestMatchers("/v3/api-docs/**").permitAll()
                        .requestMatchers("/actuator/**").permitAll()
                        .requestMatchers("/file/**").permitAll()
                        .anyRequest().authenticated()
                );

        return http.build();
    }

    @Bean
    UsernamePasswordAuthenticationFilter customUsernamePasswordAuthenticationFilter(
            AuthenticationManager authenticationManager,
            MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter
    ) {
        ObjectMapper objectMapper = mappingJackson2HttpMessageConverter.getObjectMapper();
        UsernamePasswordAuthenticationFilter filter =
                new CustomUsernamePasswordAuthenticationFilter(objectMapper);
        filter.setAuthenticationSuccessHandler(new CustomAuthenticationSuccessHandler(objectMapper));
        filter.setAuthenticationFailureHandler(new CustomAuthenticationFailureHandler(objectMapper));
        filter.setAuthenticationManager(authenticationManager);
        filter.setSecurityContextRepository(new HttpSessionSecurityContextRepository());
        return filter;
    }

    @Bean
    AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(
            PasswordEncoder passwordEncoder,
            UserDetailsService userDetailsService
    ) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private static class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

        private final ObjectMapper objectMapper;

        public CustomAuthenticationSuccessHandler(ObjectMapper objectMapper) {
            this.objectMapper = objectMapper;
        }

        @Override
        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            response.getWriter().write(objectMapper.writeValueAsString(R.success(userDetails)));
        }
    }

    private static class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

        private final ObjectMapper objectMapper;

        public CustomAuthenticationFailureHandler(ObjectMapper objectMapper) {
            this.objectMapper = objectMapper;
        }

        @Override
        public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(objectMapper.writeValueAsString(R.error(BusinessExceptionEnum.ERROR_AUTHENTICATION_FAILED)));
        }
    }

    private static class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

        private final ObjectMapper objectMapper;

        public CustomLogoutSuccessHandler(ObjectMapper objectMapper) {
            this.objectMapper = objectMapper;
        }

        @Override
        public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(objectMapper.writeValueAsString(R.success("Logout Success!")));
        }
    }

    private static class CustomAccessDeniedHandler implements AccessDeniedHandler {

        private final ObjectMapper objectMapper;

        public CustomAccessDeniedHandler(ObjectMapper objectMapper) {
            this.objectMapper = objectMapper;
        }

        @Override
        public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException{
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(objectMapper.writeValueAsString(R.error(BusinessExceptionEnum.ERROR_ACCESS_DENIED)));
        }
    }

    private static class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

        private final ObjectMapper objectMapper;

        public CustomAuthenticationEntryPoint(ObjectMapper objectMapper) {
            this.objectMapper = objectMapper;
        }
        @Override
        public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(objectMapper.writeValueAsString(R.error(BusinessExceptionEnum.ERROR_AUTHENTICATION_FAILED)));
        }
    }
}
